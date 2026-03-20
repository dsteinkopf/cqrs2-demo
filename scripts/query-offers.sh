#!/usr/bin/env bash
set -euo pipefail

QUERY_URL="${QUERY_URL:-http://localhost:8082}"
OFFER_ID="${1:-}"

if [ -n "$OFFER_ID" ]; then
    curl -s "${QUERY_URL}/api/offers/${OFFER_ID}" | python3 -m json.tool
else
    curl -s "${QUERY_URL}/api/offers" | python3 -m json.tool
fi
