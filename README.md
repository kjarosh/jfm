# Java Filesystem Mapper

[![Build Status](https://travis-ci.com/kjarosh/jfm.svg?branch=master)](https://travis-ci.com/kjarosh/jfm)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/73a0054f65344a96867c51361c9486c8)](https://www.codacy.com/app/kjarosh/jfm?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=kjarosh/jfm&amp;utm_campaign=Badge_Grade)

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
