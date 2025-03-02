[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/fZvJ74jt)

**Objetivo del Ejercicio**

Desarrollar un juego de exploración de mazmorras en Java que utilice los cuatro pilares de la programación orientada a objetos: encapsulación, herencia, polimorfismo y abstracción. El juego debe incluir clases, interfaces y la interacción entre diferentes tipos de objetos en la mazmorra.

**Descripción del Juego**

El jugador debe explorar una mazmorra compuesta de diferentes tipos de salas. Cada sala puede estar vacía, contener un tesoro o albergar un enemigo. El jugador debe navegar por estas salas, recoger objetos y enfrentarse a enemigos, todo mientras intenta mantenerse con vida.

**Reglas del Juego**

1- El jugador comienza con un nombre y una cantidad inicial de puntos de vida.

2- El jugador puede moverse a través de varias salas en la mazmorra.

3- Las salas pueden ser de tres tipos: vacía, con tesoro o con enemigo.

4- El jugador puede recoger tesoros que se añaden a su inventario.

5- El jugador puede recibir daño de los enemigos y perder puntos de vida.

6- El juego termina cuando el jugador ha explorado todas las salas o ha perdido todos sus puntos de vida.

**Requisitos Técnicos**

1- Interfaces y Abstracción: Define interfaces para las salas y los objetos interactuables en el juego.

2- Encapsulación: Utiliza atributos privados y métodos públicos para manejar el estado del jugador y de las salas.

3- Herencia: Crea clases concretas que implementen las interfaces definidas para representar diferentes tipos de salas.

4- Polimorfismo: Usa referencias de las interfaces para manejar los diferentes tipos de salas y objetos en el juego.

**Especificaciones Detalladas**

Paso 1: Crear las interfaces básicas

- Define una interfaz Room para representar una sala en la mazmorra.
- Define una interfaz GameObject para representar objetos interactuables en la mazmorra.

Paso 2: Crear la clase Player

- Define una clase Player que encapsule los datos y comportamientos del jugador.
- La clase debe incluir atributos como name, health, e inventory.
- Proporciona métodos para que el jugador pueda recibir daño, recoger objetos y mostrar su estado actual.

Paso 3: Crear clases concretas de Room

- Crea una clase EmptyRoom que represente una sala vacía.
- Crea una clase TreasureRoom que represente una sala con un tesoro.
- Crea una clase EnemyRoom que represente una sala con un enemigo.

Paso 4: Crear la clase principal del juego

- Define una clase principal que inicialice el jugador y una serie de salas.
- Implementa un bucle que permita al jugador moverse por las salas.
- Maneja las interacciones del jugador con las salas y los objetos.

**Instrucciones Adicionales**

- Asegúrate de manejar adecuadamente las interacciones entre el jugador y los diferentes tipos de salas.
- Considera agregar más salas y enemigos para hacer el juego más interesante.
- Puedes implementar características adicionales como la posibilidad de que el jugador use objetos del inventario.

# 🏰 DungeonGame

> Un juego de mazmorras basado en texto donde los jugadores exploran habitaciones, enfrentan enemigos y buscan tesoros.

[![Java](https://img.shields.io/badge/Java-8%2B-orange)](https://www.java.com/)
[![License](https://img.shields.io/badge/License-MIT-blue)](LICENSE)
[![Status](https://img.shields.io/badge/Status-En%20Desarrollo-green)](STATUS)

---

## 📋 Tabla de Contenidos

- [Descripción](#-descripción)
- [Estructura del Proyecto](#️-estructura-del-proyecto)
- [Instalación](#-instalación)
- [Cómo Jugar](#-cómo-jugar)
- [Características](#-características)
- [Licencia](#-licencia)

---

## 📝 Descripción

**Historia del Juego**

En el reino del código se extiende una leyenda acerca de un poderoso error, un bug ancestral que perturba la armonía de los programas y sistemas. La única esperanza para restaurar la paz es encontrar la llave del tesoro, que desbloquea el conocimiento y soluciona el gran bug.

El jugador, que puede ser Héroe o Heroína, asume el rol de un programador en formación que inicia como Trainee y, tras enfrentar numerosos desafíos, sube a Junior y finalmente a Senior. En su travesía, se encontrará con salas vacías, tesoros (herramientas como Git, IntelliJ, Maven, entre otras, que recuperan vida) y enemigos (los temibles Bugs) que lo desafiarán con preguntas de Java.

---

## 🗂️ Estructura del Proyecto

| Archivo/Directorio    | Descripción                                                                      |
| --------------------- | -------------------------------------------------------------------------------- |
| **main.java**         | 📄 Archivo principal ubicado en `DungeonGame/` para fácil acceso                 |
| **package player**    | 👤 Contiene `Player` y las clases de acciones como `ActionPlayer` y `MovePlayer` |
| **package rooms**     | 🚪 Implementaciones de salas (EmptyRoom, TreasureRoom, EnemyRoom)                |
| **package objects**   | 🎒 Alberga la interfaz `GameObject`                                              |
| **package enemies**   | 👾 Contiene la clase `BugEnemy`                                                  |
| **package questions** | ❓ Gestiona las preguntas y pools para niveles Trainee y Junior                  |

---

## 💻 Instalación

1. Clona el repositorio:

   ```bash
   https://github.com/Generation-Chile-Java/dungeonexplorer-gperzal.git
   ```

2. Navega al directorio del proyecto:
   ```bash
   cd DungeonGame
   ```

---

## 🎮 Cómo Jugar

### 🚀 Ejecutar el Juego

Desde la raíz del proyecto, compila y ejecuta la clase `Main.java`:

```bash
mkdir -p bin
javac -d bin src/**/*.java
java -cp bin Main
```

> **Nota**: Asegúrate de tener configurado correctamente tu entorno Java.

### 🕹️ Mecánicas del Juego

**Movimiento y Comandos**
| Comando | Acción |
|---------|--------|
| `A` | Moverse a la izquierda|
| `W` | Moverse hacia arriba |
| `D` | Moverse a la derecha |
| `S` | Moverse hacia abajo |

**Interacción**
| Comando | Acción |
|---------|--------|
| `F` | Acción (usar en caso de encontrar cobres, paredes, puertas o enemigos)|
| `E` | Mostrar el siguiente texto |
| `Q` | Mostrar el texto anterior |
| `V` | Abrir el inventario|
| `X` | Usar objeto del inventario|

**Tipos de Objetos**
| Comando | Acción |
|---------|--------|
| `Tesoros` | Representan herramientas como Git, IntelliJ, Maven, etc. que ayudan a recuperar puntos de vida.|
| `Llaves` | Utilizadas para abrir puertas y avanzar en la mazmorra. |

**Niveles del Jugador**
| Comando | Acción |
|---------|--------|
| `Trainee` | Nivel inicial, donde se enfrentan preguntas básicas.|
| `Junior` | Nivel intermedio, con desafíos y preguntas más complejas. |
| `Senior` | Nivel final, el objetivo del juego, alcanzado cuando se dominan los fundamentos del código. |

### 🗺️ Mapas

**Mapa Trainee:**

- Sala Principal: Punto de inicio.
- Desde la Sala Principal, hacia la derecha se conecta a Habitación1, que contiene la "Secreta la llave".
- Desde Habitación1, hacia arriba se conecta a Habitación Secreta, que contiene el "tesoro del conocimiento".
- Desde la Habitación Secreta, hacia arriba se transita al siguiente nivel: Desarrollador Junior.

**Mapa Junior:**

- Sala Principal: Punto de inicio.
- Desde la Sala Principal, hacia la derecha se conecta a Habitación1 (con "Tesoro Falso").
- Desde Habitación1, hacia la derecha se conecta a Habitación2 (con "Tesoro verdadero").
- Se permite regresar de Habitación2 a Habitación1 (hacia la izquierda) y de Habitación1 a Sala Principal (hacia la izquierda).
- Desde la Sala Principal, hacia arriba se conecta a Habitación Secreta (inicialmente bloqueada, que contiene el "tesoro del conocimiento").
- Desde la Habitación Secreta, hacia arriba se transita al siguiente nivel: Desarrollador Senior.

**Mapa Senior:**

- Sala Principal: Punto de inicio, con la puerta del conocimiento bloqueada.
- Desde la Sala Principal, hacia la izquierda se conecta a Sala Exterior.
- Desde Sala Exterior, hacia la izquierda se conecta a Campos Elíseos (que contiene la "llave secreta de snippet").
- Se permite regresar de Campos Elíseos a Sala Exterior (hacia la derecha) y de Sala - Exterior a Sala Principal (hacia la derecha).
- Desde la Sala Principal, hacia la derecha se conecta a Habitación1 (con puerta bloqueada, que requiere la llave de snippet).
- Desde Habitación1, hacia la derecha se conecta a Habitación Secreta (que contiene una llave).
- Desde Habitación Secreta, hacia arriba se conecta a Habitación2 (vacía, sin utilidad).
- Desde Habitación2, se puede regresar a Habitación Secreta (hacia abajo).
- Desde Habitación Secreta, hacia abajo se conecta a Habitación3 (que contiene la "llave del conocimiento").
- Se permite regresar de Habitación3 a Habitación Secreta (hacia arriba), de Habitación - Secreta a Habitación1 (hacia la izquierda) y, finalmente, de Habitación1 a Sala Principal (hacia la izquierda).
- Finalmente, en Sala Principal se utilizará la "llave del conocimiento" para desbloquear la puerta del conocimiento y obtener el "Tesoro del Conocimiento" (FIN DEL JUEGO).

---

## ✨ Características

- 🧭 **Exploración dinámica**: Descubre diferentes tipos de habitaciones con desafíos únicos
- ⚔️ **Sistema de combate basado en conocimiento**: Responde preguntas técnicas para derrotar enemigos
- 💰 **Búsqueda de tesoros**: Recolecta valiosos objetos durante tu aventura
- 📈 **Progresión por niveles**: Avanza a través de dificultades Trainee y Junior
- 🔄 **Sistema de guardado**: Guarda tu progreso y continúa tu aventura más tarde

---

## 📜 Licencia

Este proyecto está bajo la licencia MIT. Consulta el archivo [LICENSE](LICENSE) para más detalles.

---

<div align="center">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg" alt="DungeonGame Logo Java" width="50" height="50" />
  <p>Desarrollado con ❤️ por GXPZ Developer Full Stack</p>
  <p>© 2025 DungeonGame</p>
</div>
