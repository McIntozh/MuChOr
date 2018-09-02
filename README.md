# MuChOr
**Mu**lti **Ch**annel **Or**ganizer helps you create a unified interface for different sales channels.

MuChOr is still in development.

## MuChOr is not
- a ready to ship application that covers all your needs.
- made for end users, the currently is no fancy foolproof GUI.
- working for all sales channels out there, but we work on supporting more an more.

## MuChOr is
- made by developers for developers.
- requiring some initial efford to setup.
- in many ways customizable.
- open source, please feel free to contribute.
- written in Java and heavily depending on CDI.
- developed on Linux. If you're using Windows problems may occur.


## FAQ
- **Q**: I'm a sales guy, I've heard MuChOr is free. Where can I download it and how do I install it?
- A: No, MuChOr is not made for you.

-----

- **Q**: I'm not a developer, can I use MuChOr?
- A: Probably not

-----

- **Q**: I want to sell on EnterSalesPlatformNameHere(tm), can I do that with MuChOr?
- A: Probably not, see what channels are currently supported.
If you are a developer you could write a channel implementation yourself. If you do so, please submit your development.

-----

- **Q**: I've to use EnterFancyDatabaseNameHere(tm) as persistence layer, does MuChOr support that?
- A: Probably not, we currently only support MongoDB, but you may write a new peristence layer. If you do so, please submit your development.

## Inital setup
You could check out the [examples](example) section to see MuChOr in action. But you can also start from scratch (also see [example/example-plain](example/example-plain)):
### 1. Check out the git repository and run a maven build on it
```
 git clone https://github.com/McIntozh/MuChOr.git
 cd MuChOr
 mvn clean install
```
### 2. Create a new Java maven project
### 3. Add the dependencies to your `pom.xml`
Since MuChOr heavily relies on CDI, we need a library providing CDI, for example weld:

```xml
<dependency>
  <groupId>org.jboss.weld.se</groupId>
  <artifactId>weld-se</artifactId>
  <version>2.4.7.Final</version>
</dependency>
```

`core` is the dependency you always want, it contains the core MuChOr framework.

```xml
<dependency>
  <groupId>de.websplatter.muchor</groupId>
  <artifactId>core</artifactId>
  <version>0.9.1-SNAPSHOT</version>
</dependency>
```

Next you need a persistence layer. MuChOr currently only support MongoDB, so fire up a MongoDB instance on your machine and add following dependency:

```xml
<dependency>
  <groupId>de.websplatter.muchor</groupId>
  <artifactId>persistence-mongo</artifactId>
  <version>0.9.1-SNAPSHOT</version>
</dependency>
```

### 4. Create the `beans.xml`
Add a file called `beans.xml` into the `META-INF` folder (`src/main/resources/META-INF`) of your project with following contents:

```xml
<beans xmlns="http://xmlns.jcp.org/xml/ns/javaee"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/beans_1_2.xsd"
  version="1.2"
  bean-discovery-mode="annotated">
</beans>
```
### 5. Create the configuration
MuChOr is configured via a YAML. Place a new file called `muchor.yml` into the resources folder (`src/main/resources`) of your project. 

### 6. Configure persistence
To let MuChOr connect to your MongoDB instance, you have to add the according auth information to the configuration. Add following lines to the file (adjust address, user, db and pwd as needed):

```
persistence:
  mongo:
    cluster: localhost
    user: muchor
    db: muchor
    pwd: passwd
```

### 7. Create a Main class
In order to start MuChor we need a class that sets up CDI. Create a `Main` class with like the following:

```java
import java.util.Scanner;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

public class Main {
  public static void main(String[] args) {
    Weld weld = new Weld();
    try (WeldContainer container = weld.initialize()) {
      new Scanner(System.in).nextLine();
    }
  }
}
```
### 8. Run it
Run the Main class, in the log you should see Weld initializing and something like

```
...
Initializing MuChOr
...
/Initializing MuChOr
...
```
Congratulations, your project is now running and doing nothing :)
### 9. What next?
You can learn how to set up [channels](channel), how to [schedule](schedule) tasks/jobs or how to [fill](example/example-filler) MuChOr with your articles and stock.