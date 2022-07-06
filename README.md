# Java Filesystem Mapper

![Build Status](https://github.com/kjarosh/jfm/actions/workflows/maven/badge.svg)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/73a0054f65344a96867c51361c9486c8)](https://www.codacy.com/app/kjarosh/jfm?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=kjarosh/jfm&amp;utm_campaign=Badge_Grade)
[![Maintainability](https://api.codeclimate.com/v1/badges/6585b2f62e6e75643704/maintainability)](https://codeclimate.com/github/kjarosh/jfm/maintainability)
[![Release](https://jitpack.io/v/kjarosh/jfm.svg)](https://jitpack.io/#kjarosh/jfm)

## What is it?

This is a library used to map filesystem resources to Java objects and vice-versa.

## Features

- mapping filesystem resources to Java objects,
- mapping Java objects to filesystem resources,
- registering custom type handlers.

## Example

Interface:

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

### Filesystem to Java mapping

Filesystem:

```text
persons
|- person1
|  |- first-name
|  \- last-name
\- person2
   |- first-name
   \- last-name
```

The following code maps the above filesystem into instances of `PersonInfo`.

```java
Path root = Paths.get("persons");
FilesystemMapper fsmapper = FilesystemMapper.instance();

FilesystemMapperTarget person1Target = fsmapper.getTarget(root.resolve("person1"));
FilesystemMapperTarget person2Target = fsmapper.getTarget(root.resolve("person2"));

PersonInfo person1 = person1Target.proxy(PersonInfo.class);
PersonInfo person2 = person2Target.proxy(PersonInfo.class);

// print contents of persons/person1/first-name
System.out.println(person1.getFirstName());
// print contents of persons/person1/last-name
System.out.println(person1.getLastName());

// print contents of persons/person2/first-name
System.out.println(person2.getFirstName());
// print contents of persons/person2/last-name
System.out.println(person2.getLastName());
``` 

## Java to filesystem mapping

Implementation:

```java
class PersonInfoImpl implements PersonInfo {
    public String getFirstName() {
        return "John";
    }

    public String getLastName() {
        return "Smith";
    }
}
```

The following code maps the above implementation into the filesystem.

```java
Path root = Paths.get("person");
FilesystemMapper fsmapper = FilesystemMapper.instance();

FilesystemMapperTarget personTarget = fsmapper.getTarget(root);

PersonInfo person = new PersonInfoImpl();
personTarget.mount(person);
``` 

The resulting filesystem:

```text
person
|- first-name
\- last-name
```

The contents of `person/first-name` and `person/last-name` should be `John`
and `Smith` respectively.
