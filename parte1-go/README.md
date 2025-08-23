# Parte 1 — Go: Generador eficiente de números primos

Implementación en Go que calcula e imprime todos los números primos en un rango grande de forma eficiente. Usa una criba optimizada solo para números impares (bitset comprimido) para reducir memoria y acelerar el cálculo.

## ¿Qué hace?
- Imprime todos los primos en el intervalo [0, 500 000 000] por defecto.
- Puedes ajustar el límite superior modificando la constante `upper` en `main.go`.

## Requisitos
- Go 1.20 o superior (recomendado).

## Ejecutar
```bash
cd parte1-go
go run .
```

## Cambiar el rango de búsqueda
Abre `main.go` y cambia el valor de la constante `upper` para establecer el límite superior deseado.

## Notas de rendimiento
- Se usa un bitset compacto que representa solo impares (índice i => número 2*i+3), lo que reduce el uso de memoria.
- La salida se escribe con un búfer grande para minimizar el costo de E/S.
- Para pruebas rápidas, usa un límite inferior (por ejemplo, 100000 o 1e6) antes de ejecutar con valores muy grandes.
