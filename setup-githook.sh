#!/usr/bin/env bash

read -r -d '' SCRIPT_BODY <<EndOfScript
#!/usr/bin/env bash

MSG="\$1"

if ! grep -qE '^(VTCCA-[0-9]{1,4}[ ][a-zA-Z:-]+)|Merge .*' "\$MSG"; then
      tput setaf 1;
      echo "Your commit message must start with a VTCCA ticket reference, e.g. 'VTCCA-1234', followed by a SPACE"
      tput sgr0;
      echo "Allowed: VTCCA-1234 message, VTCCA-0 message, VTCCA-1234 - message"
      echo "Not allowed: [VTCCA-1234] message, VTCCA-1234-message, VTCCA-1234: message, <message without ticket ref>"
      exit 1
fi
EndOfScript

echo "$SCRIPT_BODY" > .git/hooks/commit-msg && chmod +x .git/hooks/commit-msg

