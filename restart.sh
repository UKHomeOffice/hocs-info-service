#!/bin/bash
set -euo pipefail

export KUBE_NAMESPACE=${ENVIRONMENT}

if [[ ${KUBE_NAMESPACE} == *prod ]] ; then
    export KUBE_SERVER="https://kube-api-prod.prod.acp.homeoffice.gov.uk"
    export KUBE_CERTIFICATE_AUTHORITY="https://raw.githubusercontent.com/UKHomeOffice/acp-ca/master/acp-prod.crt"
else
    export KUBE_SERVER="https://kube-api-notprod.notprod.acp.homeoffice.gov.uk"
    export KUBE_CERTIFICATE_AUTHORITY="https://raw.githubusercontent.com/UKHomeOffice/acp-ca/master/acp-notprod.crt"
fi

kd run rollout restart deployment hocs-info-service 
