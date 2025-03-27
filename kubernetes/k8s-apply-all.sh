#!/bin/bash

# Create namespace first
kubectl apply -f k8s-namespace.yaml

# Apply all other resources
kubectl apply -f k8s-db.yaml
kubectl apply -f k8s-travel-agency.yaml
kubectl apply -f k8s-recommendations.yaml
kubectl apply -f k8s-client.yaml

echo "Kubernetes resources created in mcp-demo namespace"