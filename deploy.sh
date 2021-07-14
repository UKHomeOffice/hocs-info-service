#!/bin/bash
set -euo pipefail

export KUBE_NAMESPACE=${ENVIRONMENT}
export VERSION=${VERSION}
export HOCS_INFO_SERVICE_DATA_VERSION=${HOCS_INFO_SERVICE_DATA_VERSION:-$VERSION}

echo
echo "Deploying hocs-info-service to ${ENVIRONMENT}"
echo "Service version: ${VERSION}"
echo "Data version: ${HOCS_INFO_SERVICE_DATA_VERSION}"

if [[ ${KUBE_NAMESPACE} =~ w?cs-(qa|demo|prod) ]]; then
    if ! [[ ${VERSION} == "${HOCS_INFO_SERVICE_DATA_VERSION}" ]]; then
        echo "Service and data versions don't match for namespace: ${KUBE_NAMESPACE}" > /dev/stderr
        exit 64
    fi
fi

if [[ ${KUBE_NAMESPACE} == wcs-* ]]; then
    export HOCS_DATA_REPO=hocs-data-wcs
else
    if [[ ${KUBE_NAMESPACE} == cs-* ]]; then
        export HOCS_DATA_REPO=hocs-data
    else
      # if doesn't start with WCS or CS (i.e. hocs-)
      # fallback to already set value, or cs
      export HOCS_DATA_REPO=${HOCS_DATA_REPO:-hocs-data}
    fi
fi

echo "Data repo: ${HOCS_DATA_REPO}"
echo

if [[ ${KUBE_NAMESPACE} == *prod ]]
then
    export MIN_REPLICAS="2"
    export MAX_REPLICAS="6"

    export MEMBER_REFRESH_HOUR="5"
    export IS_PROD="true"
    export UPTIME_PERIOD="Mon-Sun 04:55-23:00 Europe/London"

    export CLUSTER_NAME="acp-prod"
    export KUBE_SERVER="https://kube-api-prod.prod.acp.homeoffice.gov.uk"
else
    export MIN_REPLICAS="1"
    export MAX_REPLICAS="2"

    export MEMBER_REFRESH_HOUR="8" # notprod starts later than prod.
    export IS_PROD=""
    export UPTIME_PERIOD="Mon-Fri 07:55-18:00 Europe/London"

    export CLUSTER_NAME="acp-notprod"
    export KUBE_SERVER="https://kube-api-notprod.notprod.acp.homeoffice.gov.uk"
fi

export KUBE_CERTIFICATE_AUTHORITY="https://raw.githubusercontent.com/UKHomeOffice/acp-ca/master/${CLUSTER_NAME}.crt"

cd kd || exit 1

kd --timeout 10m \
    -f accountSettingsConfigMap.yaml \
    -f deployment.yaml \
    -f service.yaml \
    -f refreshmembers.yaml \
    -f autoscale.yaml
