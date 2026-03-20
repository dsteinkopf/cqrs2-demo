#!/usr/bin/env bash
set -euo pipefail

OFFER_ID="${1:?Usage: $0 <offerId> <title> <price>}"
TITLE="${2:?Usage: $0 <offerId> <title> <price>}"
PRICE="${3:?Usage: $0 <offerId> <title> <price>}"

RABBITMQ_URL="${RABBITMQ_URL:-http://localhost:15672}"
RABBITMQ_USER="${RABBITMQ_USER:-guest}"
RABBITMQ_PASS="${RABBITMQ_PASS:-guest}"

PAYLOAD=$(cat <<EOF
{"offerId":"${OFFER_ID}","title":"${TITLE}","price":${PRICE}}
EOF
)

curl -s -u "${RABBITMQ_USER}:${RABBITMQ_PASS}" \
  -X POST "${RABBITMQ_URL}/api/exchanges/%2F/offer.commands/publish" \
  -H "Content-Type: application/json" \
  -d "$(cat <<EOF
{
  "routing_key": "offer.update",
  "payload": $(echo "$PAYLOAD" | python3 -c 'import sys,json; print(json.dumps(sys.stdin.read().strip()))'),
  "payload_encoding": "string",
  "properties": {
    "content_type": "application/json",
    "headers": {
      "__TypeId__": "com.redteclab.cqrs2.command.application.UpdateOfferCommand"
    }
  }
}
EOF
)"

echo ""
echo "Sent UpdateOfferCommand: offerId=${OFFER_ID}, title=${TITLE}, price=${PRICE}"
