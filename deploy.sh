#!/bin/bash
set -euo pipefail

export KUBE_NAMESPACE=${ENVIRONMENT}
export VERSION=${VERSION}
export HOCS_INFO_SERVICE_DATA_VERSION=${HOCS_INFO_SERVICE_DATA_VERSION:-$VERSION}

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

if [[ ${KUBE_NAMESPACE} == *prod ]]
then
    export MIN_REPLICAS="2"
    export MAX_REPLICAS="6"

    export MEMBER_REFRESH_HOUR="22"

    export CLUSTER_NAME="acp-prod"
    export KUBE_SERVER="https://kube-api-prod.prod.acp.homeoffice.gov.uk"
else
    export MIN_REPLICAS="1"
    export MAX_REPLICAS="2"

    export MEMBER_REFRESH_HOUR="17" # notprod turns off earlier than prod

    export CLUSTER_NAME="acp-notprod"
    export KUBE_SERVER="https://kube-api-notprod.notprod.acp.homeoffice.gov.uk"
fi

export KUBE_CERTIFICATE_AUTHORITY="https://raw.githubusercontent.com/UKHomeOffice/acp-ca/master/${CLUSTER_NAME}.crt"

cd kd || exit 1

kd --timeout 10m \
    -f deployment.yaml \
    -f service.yaml \
    -f refreshmembers.yaml \
    -f autoscale.yaml
