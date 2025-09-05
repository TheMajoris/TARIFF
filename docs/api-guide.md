# API Guide

This API guide is mainly for TARIFF developers.

## Definitions

| Keywords | Definition |
| --- | --- |
| Internal API | Represents _all_ the APIs that are used by TCS. In the docs, these are marked with `x-internal: true` attribute. |
| Public API | Represents the APIs that do not require authentication. |
| Private API | Represents the APIs that require authentication. |

## Usage guide

1. Use the [swagger online editor](https://dsaid.stoplight.io/studio/tcs-ui) and import the `docs/api.json` file to edit the API documentation.

2. Create a PR to the `main` branch for the changes made to the API documentation.
