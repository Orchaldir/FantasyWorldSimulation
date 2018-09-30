# ADR 0002 - Use Mockito for testing

**Status:** accepted

**Date:** 2018-09-30

## Context and Problem Statement

Unit tests should only test a single class,
but many classes internally call functions of other classes.

How can unit tests only test a single class?

## Considered Options

* Create mock implementations for used interfaces.
* Use an existing mocking framework.
    * e.g. Mockito

## Decision Outcome

**Chosen option:** Mockito

* No unnecessary mock implementations.
* Test against interfaces instead of (mock) implementations.
* Mockito is more powerful than mock implementations.

## Links

* https://site.mockito.org/