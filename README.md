# Jpa
## Rapport de TP 2
* Nom : OURAHMA.
* Prènom : Maroua.
* Filière : Master en Intelligence artificielle et sciences de données
* Universitè : Facultès des sciences Universitè Moulay Ismail Meknès.

## **1- Introduction**
Dans le cadre de ce projet, nous allons explorer la gestion des rôles et utilisateurs dans un système d’authentification basé sur une architecture relationnelle. L'objectif principal est de modéliser les relations entre les utilisateurs et les rôles , en utilisant une association `ManyToMany` (plusieurs à plusieurs) pour permettre à un utilisateur d’avoir plusieurs rôles et à un rôle d'être assigné à plusieurs utilisateurs.

## **2- Enoncé**
Un système d’authentification nécessite souvent une gestion flexible des rôles et des utilisateurs . Chaque utilisateur peut être affecté à plusieurs rôles (par exemple, admin, moderator, user), et chaque rôle peut être partagé par plusieurs utilisateurs. Pour cela, il est nécessaire de définir une relation `ManyToMany` entre les entités User et Role.

**Objectifs :**
1. Modéliser deux entités principales : User et Role.
2. Implémenter une relation ManyToMany entre ces deux entités.
3. Gérer l’assignation dynamique de rôles aux utilisateurs.
4. Permettre la récupération des utilisateurs avec leurs rôles associés.


## **3- Conception**
Le diagramme de classes présenté ci-dessous a été conçu à l'aide de l'outil **Enterprise Architect**. Il modélise les entités principales du système et leurs relations, en mettant en avant les associations ManyToMany dans la conception de cette application.

![Digramme de classe](screenshots/conception.png)

### Description du diagramme

#### Entité `User`
- **Attributs** :
  - `userId` : identifiant unique.
  - `username` : nom d’utilisateur.
  - `password` : mot de passe.
- **Relation** :
  - Une liste de `roles` (association **ManyToMany**).

#### Entité `Role`
- **Attributs** :
  - `id` : identifiant unique.
  - `desc` : description du rôle.
  - `roleName` : nom du rôle.
- **Relation** :
  - Une liste de `users` (association **ManyToMany**).


## **4- Code source**

### **Entities**:
-   Chaque classe du package `entities` correspond à une table en base de données.
1. #### **La classe `Role`**:

La classe `Role` représente un **rôle dans un système d'authentification**, permettant d’attribuer des droits ou permissions à des utilisateurs. Elle est conçue pour être liée à la classe `User` via une relation **ManyToMany**, car :
- Un utilisateur peut avoir plusieurs rôles.
- Un rôle peut être attribué à plusieurs utilisateurs.

---

```java
package net.ourahma.jpafs.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
@Entity
@Data
@NoArgsConstructor @AllArgsConstructor
public class Role {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "DESCRIPTION")
    private String desc;
    @Column(length = 20, unique =true)
    private String roleName;
    @ManyToMany( fetch = FetchType.EAGER)
    //@JoinTable(name ="USERS_ROLES")
    @ToString.Exclude
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<User> users = new ArrayList<User>();
}

```
**Explication des annotations JPA et Lombok**

- `@Entity` : Indique que cette classe est une entité JPA, donc mappée à une table en base de données.

- `@Data` (Lombok) : Génère automatiquement les getters, setters, `toString`, `equals`, `hashCode`, etc.

- `@NoArgsConstructor` (Lombok) : Génère un constructeur sans arguments.

- `@AllArgsConstructor` (Lombok) : Génère un constructeur avec tous les champs.

- `@Id @GeneratedValue(strategy = GenerationType.IDENTITY)` : Définit le champ `id` comme clé primaire avec auto-incrémentation.

- `@Column(name = "DESCRIPTION")` : Spécifie que le champ `desc` sera mappé à une colonne nommée `DESCRIPTION` dans la base de données.

- `@Column(length = 20, unique = true)` : Contraint le champ `roleName` à faire au maximum 20 caractères, et à être unique dans la base.

- `@ManyToMany(fetch = FetchType.EAGER)` : Déclare une relation ManyToMany avec la classe `User`. Le chargement est fait **immédiatement** (`EAGER`) lors de la récupération d’un rôle.

- `@ToString.Exclude` : Empêche la sérialisation récursive lors de l’appel à `toString()` (évite les boucles infinies).

- `@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)` : Indique que ce champ ne doit pas apparaître dans les réponses JSON (lecture), mais reste accessible en écriture.

> La ligne `//@JoinTable(name ="USERS_ROLES")` est commentée. Cela signifie que la table intermédiaire sera gérée automatiquement par JPA. Pour personnaliser son nom ou sa structure, il suffit de décommenter cette ligne et d’ajouter les colonnes de jointure.


2. #### **La classe `User`**:

La classe `User` représente un utilisateur du système d’authentification. Elle est associée à la classe `Role` via une relation **ManyToMany**, permettant à chaque utilisateur d’avoir plusieurs rôles (ex : `admin`, `user`...).  
Elle contient les informations essentielles d’un utilisateur, tout en assurant la sécurité du mot de passe lors des sérialisations.
```java
package net.ourahma.jpafs.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name= "USERS")
@Data @NoArgsConstructor @AllArgsConstructor
public class User {
    @Id
    private String userId;
    @Column(name="USER_NAME",unique=true,length = 20)
    private String username;
    // on souhaite pas envoyer le password 
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @ManyToMany(mappedBy = "users",fetch = FetchType.EAGER)
    private List<Role> roles = new ArrayList<Role>();

}

```
**Explication des annotations JPA et Lombok**

- `@Entity` : Indique que cette classe est une entité JPA.
- `@Table(name = "USERS")` : Spécifie que cette entité est mappée à la table nommée `"USERS"` dans la base de données.
- `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor` : Annotations de Lombok permettant de générer automatiquement les getters, setters, constructeurs, etc.

- `@Id` : Déclare le champ `userId` comme clé primaire.
- `@Column(name="USER_NAME", unique=true, length=20)` : 
  - Le champ `username` est mappé à la colonne `USER_NAME`.
  - Il doit être **unique** et ne pas dépasser **20 caractères**.

- `@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)` :
  - Empêche l’envoi du mot de passe dans les réponses JSON.
  - Autorise sa lecture uniquement lors de la réception des requêtes (utile pour la sécurité).

- `@ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)` :
  - Définit une relation bidirectionnelle avec `Role`.
  - L’attribut `mappedBy = "users"` indique que c’est la classe `Role` qui gère la relation.
  - Le chargement se fait immédiatement (`fetch = FetchType.EAGER`), ce qui facilite l’accès aux rôles sans charger manuellement.

> La liste `roles` est initialisée avec une nouvelle instance d’`ArrayList` pour éviter les problèmes de `NullPointerException`.




### **Repositories**:

1. #### **L'interface `RoleRepository`**:
L'interface `RoleRepository` étend `JpaRepository<Role, Long>`, ce qui lui permet d’hériter automatiquement de toutes les méthodes CRUD de Spring Data JPA.
```java
package net.ourahma.jpafs.repositories;

import net.ourahma.jpafs.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleName(String roleName);
}

```
**Méthodes personnalisées**:
```java
Role findByRoleName(String roleName);
```
- Cette méthode permet de rechercher un rôle par son nom (`roleName`) dans la base de données.
- Spring Data JPA génère automatiquement l’implémentation de cette méthode en se basant sur le nom du champ (roleName) défini dans l’entité Role.

2. #### **L'interface UserRepository**:
L'interface `UserRepository` étend `JpaRepository<User, String>`, ce qui lui permet d’hériter automatiquement de toutes les méthodes CRUD de Spring Data JPA.
```java
package net.ourahma.jpafs.repositories;

import net.ourahma.jpafs.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    User findByUsername(String username);
}

```
- La méthode `findByUsername` permet de rechercher un utilisateur par son nom d’utilisateur (`username`).

### Service:
Le package `service` contient les classes et interfaces qui implémentent la **logique métier** de l’application.  
Il sert d’intermédiaire entre les contrôleurs (ou autres couches d’entrée) et les repositories, en gérant les opérations sur les données avant leur persistance ou leur retour au client.

---

## 1. **Interface `UserService`**

L'interface `UserService` définit un ensemble de méthodes abstraites permettant de gérer les utilisateurs et les rôles dans le système d’authentification. 

```java
package net.ourahma.jpafs.service;

import net.ourahma.jpafs.entities.Role;
import net.ourahma.jpafs.entities.User;

public interface UserService  {
    User addNewUser(User user);
    Role addNewRole(Role role);
    User findUserByUserName(String userName);
    Role findRoleByRoleName(String roleName);
    void addRoleToUser(String userName, String roleName);
    User authenticate(String userName, String password);
}
```
**Les méthodes exposées**

- **`addNewUser(User user)`** :  Permet d’ajouter un nouvel utilisateur au système.

- **`addNewRole(Role role)`** :  Permet de créer un nouveau rôle dans le système.

- **`findUserByUserName(String userName)`** :  Recherche un utilisateur par son nom d’utilisateur.

- **`findRoleByRoleName(String roleName)`** :  Recherche un rôle par son nom.

- **`addRoleToUser(String userName, String roleName)`** :  Associe un rôle existant à un utilisateur existant (gestion de la relation ManyToMany).

- **`authenticate(String userName, String password)`** :  Permet de vérifier les identifiants d’un utilisateur lors de la connexion.

## 2. Implémentation du service : `UserServiceImpl`:

La classe `UserServiceImpl` est l’implémentation concrète de l’interface `UserService`.

```java
package net.ourahma.jpafs.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import net.ourahma.jpafs.entities.Role;
import net.ourahma.jpafs.entities.User;
import net.ourahma.jpafs.repositories.RoleRepository;
import net.ourahma.jpafs.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor // a ne pas mettre le constructeur sans parametre, erreur dans l'injection de dépendances
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    /*public UserServiceImpl(@Qualifier UserRepository userRepository,
                            @Qualifier RoleRepository roleRepository) {
        this.userRepository = userRepository;
    }
    on peut utiliser Autowired , ou le constructeur et l'annotation
    AllArgConstructor
    */

    @Override
    public User addNewUser(User user) {
        user.setUserId(UUID.randomUUID().toString());
        return userRepository.save(user);
    }

    @Override
    public Role addNewRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public User findUserByUserName(String userName) {
        return userRepository.findByUsername(userName);
    }

    @Override
    public Role findRoleByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }

    @Override
    public void addRoleToUser(String userName, String roleName) {
    User user = this.findUserByUserName(userName);
    Role role = this.findRoleByRoleName(roleName);
    if(user.getRoles()!=null) {
        user.getRoles().add(role);
        // si nous avons pas intialiser la liste roles on aura un NullPointerException
        //userRepository.save(user); ce n'est pas necessaire parce que on a que la methode est transactionnelle
        role.getUsers().add(user);
    }
    }

    @Override
    public User authenticate(String userName, String password) {
        User user = userRepository.findByUsername(userName);
        if (user == null ) throw new RuntimeException("Bad Credentials");
        if(user.getPassword().equals(password)) {
            return user;
        }
        throw new RuntimeException("Bad Credentials");
    }
}

```

**Les annptations**

- `@Service` : Indique que cette classe est un composant Spring qui contient de la logique métier. Elle sera automatiquement détectée par Spring lors du scan.

- `@Transactional` : Garantit que chaque méthode s’exécute dans une transaction JPA. Si une erreur survient, les opérations sont annulées (rollback). Cela évite également des appels explicites à `save()` si les modifications sont faites sur des objets persistants.

- `@AllArgsConstructor` : Génère un constructeur avec tous les champs (injection des dépendances via constructeur), ce qui facilite l’injection automatique avec Spring.

> 💡 **Note :** L’utilisation de `@AllArgsConstructor` est justifiée ici car elle simplifie l’injection des dépendances sans avoir à écrire manuellement un constructeur.

---

**Méthodes implémentées**

- **`addNewUser(User user)`**  
  - Génère un ID unique (`UUID`) pour le nouvel utilisateur.
  - Sauvegarde l'utilisateur dans la base de données.

- **`addNewRole(Role role)`**  
  - Enregistre un nouveau rôle dans la base de données.

- **`findUserByUserName(String userName)`**  
  - Recherche un utilisateur par son nom d’utilisateur grâce au repository.

- **`findRoleByRoleName(String roleName)`**  
  - Cherche un rôle par son nom dans la base.

- **`addRoleToUser(String userName, String roleName)`**  
  - Associe un rôle existant à un utilisateur existant.
  - Charge les deux entités via leurs méthodes de recherche.
  - Met à jour les deux côtés de la relation ManyToMany (`user.getRoles().add(role)` et `role.getUsers().add(user)`).
  
  > Il est important de vérifier que la liste `roles` de l’utilisateur n’est pas `null` avant d’ajouter un rôle, afin d’éviter une `NullPointerException`.

- **`authenticate(String userName, String password)`**  
  - Vérifie si un utilisateur existe avec le nom d’utilisateur fourni.
  - Compare le mot de passe saisi avec celui stocké en base.
  - Lance une exception si les identifiants sont incorrects.

---
### Web :
Le package `web` contient les contrôleurs Spring (`@RestController`) qui gèrent les requêtes HTTP entrantes.  
Il constitue la couche de présentation de l’application.

1. **Classe `UserController`**

Cette classe est un contrôleur REST qui expose une API pour récupérer la liste des utilisateurs depuis la base de données.

```java
package net.ourahma.jpafs.web;

import lombok.AllArgsConstructor;
import net.ourahma.jpafs.entities.User;
import net.ourahma.jpafs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    // Consulter un utilisateur
    @GetMapping("/users/{username}")
    public User user(@PathVariable String username){
        User u = userService.findUserByUserName(username);
        return u;
    }
}
```
**Les annotations**:
- **`@RestController`** : Combine `@Controller` et `@ResponseBody`. Cela permet de renvoyer directement des objets sérialisés en JSON dans les réponses HTTP.

- **`@Autowired`** : Injecte automatiquement une instance de `UserService`.

- **`@GetMapping("/users/{username}")`** : Définit une route GET accessible via l’URL `/users/{username}`, où `{username}` est un paramètre dynamique.

- **`@PathVariable`** : Permet d’extraire la variable d’URL `{username}` et de l’utiliser comme argument de la méthode.

**Les méthodes**:
- **`user(String username)`** :
  - Recherche un utilisateur via le service métier (`userService.findUserByUserName(username)`).
  - Retourne l’utilisateur trouvé, qui sera automatiquement converti en JSON.
## **5- Captures écrans**

**Création des utilisateurs :**

```java
// créer un utilisateur
User u = new User();
u.setUsername("user1");
u.setPassword("123456");
userService.addNewUser(u);

User u2 = new User();
u2.setUsername("admin");
u2.setPassword("123456");
userService.addNewUser(u2);
System.out.println("Récupération des utilisateurs après création ");
List<User> users = userRepository.listUsers();
users.forEach(user -> {
    System.out.println("User => "+user.getUsername());
});
```
**Le résultat**:

![](screenshots/creerusers.png)

---

- **Création des rôles**:

```java
// créer des roles
Stream.of("STUDENT", "USER", "ADMIN").forEach(r->{
    Role role1 = new Role();
    role1.setRoleName(r);
    userService.addNewRole(role1);
});
System.out.println("Récupération des roles après création ");
List<Role> roles = roleRepository.findAll();
roles.forEach(role -> {
    System.out.println("Role => "+role.getRoleName());
});
```
**Le résultat**

![](screenshots/creerrole.png)

---
- **Affectation des rôles**
```java
 // affecter les roles au users
userService.addRoleToUser("user1","STUDENT");
userService.addRoleToUser("user1","USER");
userService.addRoleToUser("admin","USER");
userService.addRoleToUser("admin","ADMIN");
```

---

- **Authentifier un utilisateur, lui affecter des rôles et récupérer les utilisateurs**
```java
// consulter une utilisateur

try {
    User user = userService.authenticate("user1","123456");
    System.out.println(user.getUserId());
    System.out.println(user.getUsername());
    System.out.println("Roles => ");
    user.getRoles().forEach(role ->{
        System.out.println("Role =>"+role);
    });
}catch (Exception e) {
    e.printStackTrace();
}
```
**Le résultat**:

![](screenshots/affecterrole.png)

---

- **Migration vers MySQL :**
Pour ce faire, il suffit de changer les proproètes dans le fichier `application.properties`, sans aucun changement dans le code, voici la base de données après migration en utilisant `phpMyAdmin`:
```
spring.application.name=Jpa-fs
# spring.h2.console.enabled=true
# spring.datasource.url=jdbc:h2:mem:user_db
server.port=8083
spring.datasource.url=jdbc:mysql://localhost:3306/users_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MariaDBDialect
spring.jpa.show-sql=true
```

- **Le résultat**
    - La base de données est crée avec les tables `role`,`users` et `role_users`
![](screenshots/mysql.png)

    - Et le code se réxecute dans aucun problème:
    - ![](screenshots/consolesql.png)
## **6- Conclusion**

Ce projet a permis d’explorer les fonctionnalités offertes par JPA : ajout, consultation, mise à jour et suppression des données. Appliquer principalement dans la gestion des rôles des utilisateurs, La migration vers MySQL s’est effectuée facilement en modifiant simplement la configuration dans `application.properties`, démontrant ainsi la flexibilité de Spring Data JPA face au changement de SGBD.

## **7- Auteur**

- **Nom:**  OURAHMA
- **Prénom:** Maroua
- **Courriel:** [Email](mailto:marouaourahma@gmail.com)
- **LinkedIn:** [Linkedin](www.linkedin.com/in/maroua-ourahma)