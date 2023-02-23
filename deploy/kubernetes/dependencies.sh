#!/usr/bin/env bash

# Wait for k8s resource to exist. See: https://github.com/kubernetes/kubernetes/issues/83242
waitFor() {
  xtrace=$(set +o|grep xtrace); set +x
  local ns=${1?namespace is required}; shift
  local type=${1?type is required}; shift

  echo "Waiting for $type $*"
  until kubectl -n "$ns" get "$type" "$@" -o=jsonpath='{.items[0].metadata.name}' >/dev/null 2>&1; do
    echo "Waiting for $type $*"
    sleep 1
  done
  eval "$xtrace"
}


DIRNAME=$(dirname "$0")

kubectl create ns demo

# Deploy MySQL and wait
kubectl -n demo apply -f $DIRNAME/mysql.yaml
waitFor demo deployment mysql
kubectl -n demo wait --timeout=60s --for=condition=available deployment/mysql

# Deploy Gingersnap Cache
kubectl -n demo apply -f deploy/kubernetes/gingersnap.yaml
kubectl -n demo wait --timeout=60s --for=condition=ready caches.gingersnap-project.io/cache
