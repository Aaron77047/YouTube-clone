# Security Policy

## Supported Versions

This project is currently at version 1.0.0. Security fixes are provided for the latest code on the `main` branch and the current Android release build.

| Version / target | Supported |
| ---------------- | --------- |
| `main` branch | ✅ |
| 1.0.0 release | ✅ |
| Older or modified builds | ❌ |

## Reporting a Vulnerability

Please do not report security vulnerabilities through public GitHub issues or pull requests.

To report a vulnerability, use one of these methods:

1. Open a private GitHub security advisory under this repository's **Security** tab, if available.
2. Contact the repository owner through their [GitHub profile](https://github.com/Aaron77047).

Include:

- A description of the issue and affected component
- Steps to reproduce or a proof of concept
- The Android/API level and app build involved
- The potential impact
- Any suggested mitigation

## Response Expectations

You should receive an acknowledgement within 7 days. After triage, maintainers will work to confirm the issue, determine severity, develop a fix, and coordinate disclosure when appropriate. Resolution times vary depending on complexity and maintainers' availability.

## Security Considerations

- Keep API keys, Firebase credentials, keystores, passwords, and `.env` files out of source control.
- Treat uploaded media and user-generated comments as untrusted content.
- Use Android platform permissions and Firebase/App Check rules according to least privilege.
- Keep dependencies, the Android SDK, and Gradle tooling up to date.
- Production builds should use secrets supplied securely at build or runtime rather than hardcoded values.

## Disclosure

Please allow reasonable time for investigation and remediation before public disclosure. Do not access accounts, data, or systems beyond what is necessary to demonstrate the vulnerability.
