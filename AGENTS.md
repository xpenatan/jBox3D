# AGENTS.md

## Validation Is Required

When the user asks for a change, the work is not done until the requested workflow has been validated as working.

For implementation tasks:
- Run the Gradle task, app, sample, or command the user is expected to use.
- If validation fails because a required generated/native artifact is missing, build or fix that prerequisite when possible.
- Do not treat an expected failure as a successful result unless the user explicitly asked for partial setup only.
- If full validation is impossible because of a missing external toolchain or environment setup, report the exact blocker and the closest passing validation that was completed.
