# Parte 1 — C++: Buscador de números primos

Implementación en C++ del ejercicio de búsqueda de primos. Utiliza una criba segmentada (segmented sieve) con un único hilo para encontrar e imprimir números primos de forma eficiente en rangos grandes.

## ¿Qué hace?
- Calcula e imprime todos los números primos en un intervalo [a, b].
- Por defecto, el rango es [0, 500 000 000]. Puedes cambiarlo editando `src/main.cpp` (variables `a` y `b`).

## Requisitos
- `g++` con soporte C++17.
- `make`.

## Compilar
```bash
make -C parte1-cpp
```

## Ejecutar
```bash
make -C parte1-cpp run
```

## Cambiar el rango de búsqueda
Edita el archivo `src/main.cpp` y ajusta las variables `a` (límite inferior) y `b` (límite superior).

## Notas
- La criba segmentada reduce el uso de memoria y mejora el rendimiento cuando el intervalo es grande.
- La salida es una lista de primos, un número por línea, enviada a la salida estándar.
