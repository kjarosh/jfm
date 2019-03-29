# Java Filesystem Mapper

[![Build Status](https://travis-ci.com/kjarosh/jfm.svg?branch=master)](https://travis-ci.com/kjarosh/jfm)

## What is it?

This is a library used to map filesystem resources to Java objects.

## Features

- mapping filesystem resources to Java objects,
- registering custom type handlers.

## Example interface

```java
@FilesystemResource
interface PersonInfo {
    @Read
    @Path("first-name")
    String getFirstName();
    
    @Read
    @Path("last-name")
    String getLastName();
}
```
