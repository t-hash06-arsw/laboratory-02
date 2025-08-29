Escuela Colombiana de Ingeniería

Arquitecturas de Software – ARSW

####Taller – programación concurrente, condiciones de carrera y sincronización de hilos. EJERCICIO INDIVIDUAL O EN PAREJAS.

#####Parte I – Antes de terminar la clase.

Creación, puesta en marcha y coordinación de hilos.

1. Revise el programa “primos concurrentes” (en la carpeta parte1), dispuesto en el paquete edu.eci.arsw.primefinder. Este es un programa que calcula los números primos entre dos intervalos, distribuyendo la búsqueda de los mismos entre hilos independientes. Por ahora, tiene un único hilo de ejecución que busca los primos entre 0 y 30.000.000. Ejecútelo, abra el administrador de procesos del sistema operativo, y verifique cuantos núcleos son usados por el mismo.

2. Modifique el programa para que, en lugar de resolver el problema con un solo hilo, lo haga con tres, donde cada uno de éstos hará la tarcera parte del problema original. Verifique nuevamente el funcionamiento, y nuevamente revise el uso de los núcleos del equipo.

3. Lo que se le ha pedido es: debe modificar la aplicación de manera que cuando hayan transcurrido 5 segundos desde que se inició la ejecución, se detengan todos los hilos y se muestre el número de primos encontrados hasta el momento. Luego, se debe esperar a que el usuario presione ENTER para reanudar la ejecución de los mismo.


Solución Parte I

- Archivos clave:
    - `parte1/src/main/java/edu/eci/arsw/primefinder/Main.java`
    - `parte1/src/main/java/edu/eci/arsw/primefinder/PrimeFinderThread.java`

1) Verificación inicial de uso de núcleos
- Se ejecutó la versión base y se observó el uso de CPU con el monitor del sistema. Con un único hilo, el proceso usa esencialmente 1 núcleo lógico.

2) Paralelización con 3 hilos
- En `Main.java` se configuró `threadCount = 3` y se dividió el rango [0, 30.000.000] en tercios, asignando a cada hilo su sub-rango. Cada hilo instancia `PrimeFinderThread(start, end, "Hilo-i")` y se inicia con `start()`.
- Resultado: ahora se observan 3 hilos de cómputo trabajando en paralelo, aprovechando múltiples núcleos del equipo.

3) Pausa a los 5 segundos, reporte parcial y reanudación con ENTER
- En `Main.java` se agregó lógica para:
    - Dormir el hilo principal 5s (`Thread.sleep(5000)`),
    - Pausar todos los hilos de cómputo llamando a `PrimeFinderThread.pausarTodos()`,
    - Sumar el tamaño de las listas parciales de primos de cada hilo (`t.getPrimes().size()`) y mostrar el total parcial,
    - Esperar un ENTER por consola,
    - Reanudar la ejecución con `PrimeFinderThread.continuarTodos()` y luego hacer `join()` a los hilos para esperar su finalización.
- En `PrimeFinderThread.java` se implementó un monitor global con `wait/notifyAll`:
    - Campos estáticos: `pausado` y `monitor` para coordinar todos los hilos.
    - En el bucle de búsqueda se llama a `esperarSiPausado()` en cada iteración, que bloquea el hilo mientras `pausado` sea `true`.
    - Métodos estáticos `pausarTodos()` y `continuarTodos()` cambian el estado y notifican a todos los hilos.

Cómo ejecutar Parte I

- Construir y ejecutar desde `parte1/`:

    mvn -DskipTests package
    java -cp target/primesearch-1.0-SNAPSHOT.jar edu.eci.arsw.primefinder.Main

- Comportamiento esperado:
    - Tras 5s se imprime el total parcial de primos encontrados y el programa queda esperando ENTER.
    - Al presionar ENTER, los hilos continúan hasta terminar y se imprime el total final de primos.



#####Parte II 


Para este ejercicio se va a trabajar con un simulador de carreras de galgos (carpeta parte2), cuya representación gráfica corresponde a la siguiente figura:

![](./img/media/image1.png)

En la simulación, todos los galgos tienen la misma velocidad (a nivel de programación), por lo que el galgo ganador será aquel que (por cuestiones del azar) haya sido más beneficiado por el *scheduling* del
procesador (es decir, al que más ciclos de CPU se le haya otorgado durante la carrera). El modelo de la aplicación es el siguiente:

![](./img/media/image2.png)

Como se observa, los galgos son objetos ‘hilo’ (Thread), y el avance de los mismos es visualizado en la clase Canodromo, que es básicamente un formulario Swing. Todos los galgos (por defecto son 17 galgos corriendo en una pista de 100 metros) comparten el acceso a un objeto de tipo
RegistroLLegada. Cuando un galgo llega a la meta, accede al contador ubicado en dicho objeto (cuyo valor inicial es 1), y toma dicho valor como su posición de llegada, y luego lo incrementa en 1. El galgo que
logre tomar el ‘1’ será el ganador.

Al iniciar la aplicación, hay un primer error evidente: los resultados (total recorrido y número del galgo ganador) son mostrados antes de que finalice la carrera como tal. Sin embargo, es posible que una vez corregido esto, haya más inconsistencias causadas por la presencia de condiciones de carrera.

Taller.

1.  Corrija la aplicación para que el aviso de resultados se muestre
    sólo cuando la ejecución de todos los hilos ‘galgo’ haya finalizado.
    Para esto tenga en cuenta:

    a.  La acción de iniciar la carrera y mostrar los resultados se realiza a partir de la línea 38 de MainCanodromo.

    b.  Puede utilizarse el método join() de la clase Thread para sincronizar el hilo que inicia la carrera, con la finalización de los hilos de los galgos.

2.  Una vez corregido el problema inicial, corra la aplicación varias
    veces, e identifique las inconsistencias en los resultados de las
    mismas viendo el ‘ranking’ mostrado en consola (algunas veces
    podrían salir resultados válidos, pero en otros se pueden presentar
    dichas inconsistencias). A partir de esto, identifique las regiones
    críticas () del programa.

3.  Utilice un mecanismo de sincronización para garantizar que a dichas
    regiones críticas sólo acceda un hilo a la vez. Verifique los
    resultados.

4.  Implemente las funcionalidades de pausa y continuar. Con estas,
    cuando se haga clic en ‘Stop’, todos los hilos de los galgos
    deberían dormirse, y cuando se haga clic en ‘Continue’ los mismos
    deberían despertarse y continuar con la carrera. Diseñe una solución que permita hacer esto utilizando los mecanismos de sincronización con las primitivas de los Locks provistos por el lenguaje (wait y notifyAll).


Solución Parte II

- Archivos clave:
    - `parte2/src/main/java/arsw/threads/MainCanodromo.java`
    - `parte2/src/main/java/arsw/threads/RegistroLlegada.java`
    - `parte2/src/main/java/arsw/threads/Galgo.java`

1) Mostrar resultados solo al finalizar todos los galgos
- En `MainCanodromo.java`, dentro del hilo que arranca la carrera, después del `start()` de cada `Galgo`, se añadió un bucle `for (Galgo g : galgos) { g.join(); }` para esperar a que todos terminen antes de mostrar el diálogo de resultados (`winnerDialog`). Así se evita mostrar resultados prematuros.

2) Inconsistencias y regiones críticas
- Problemas observados al correr múltiples veces:
    - Posiciones de llegada duplicadas o saltadas.
    - Ganador no siempre coherente con la primera posición registrada.
- Regiones críticas identificadas:
    - Lectura e incremento de `ultimaPosicionAlcanzada` (operación compuesta que debe ser atómica).
    - Asignación del `ganador` (solo debe hacerse una vez y por el primer hilo que llegue).

3) Sincronización de regiones críticas
- En `RegistroLlegada.java` se crearon métodos sincronizados:
    - `synchronized int tomarYAvanzarPosicion()`: lee la posición actual y la incrementa de manera atómica, devolviendo la posición tomada por el galgo.
    - `synchronized void intentarRegistrarGanador(String nombre, int posicion)`: si `posicion == 1` y no hay ganador aún, fija el ganador.
- En `Galgo.java`, al cruzar la meta, se invoca:
    - `int ubicacion = regl.tomarYAvanzarPosicion();`
    - `regl.intentarRegistrarGanador(this.getName(), ubicacion);`
- Resultado: orden de llegada consistente, sin duplicados, y ganador estable.

4) Pausa y continuar con wait/notifyAll
- En `RegistroLlegada.java` se añadió un monitor común y estado `pausado` con métodos:
    - `pausar()` y `continuar()` para cambiar el estado y notificar a todos los hilos.
    - `esperarSiPausado()` que bloquea en un `while (pausado) wait()` hasta reanudación.
- En `Galgo.java` se llama `regl.esperarSiPausado()` al inicio de cada iteración del bucle de carrera para obedecer el estado de pausa.
- En `MainCanodromo.java` se conectaron los botones:
    - ‘Stop’ -> `reg.pausar()`
    - ‘Continue’ -> `reg.continuar()`
- Resultado: la carrera se detiene casi inmediatamente al pulsar Stop y se reanuda al pulsar Continue, sin busy-wait ni bloqueos de la UI.

Cómo ejecutar Parte II

- Construcción y ejecución:
    - Asegúrese de usar Java 8+ para compilar. Si el compilador rechaza source/target 1.7, ajuste el `pom.xml` a 1.8.

    mvn -DskipTests package -f parte2/pom.xml
    mvn exec:java -Dexec.mainClass=arsw.threads.MainCanodromo -f parte2/pom.xml

- Comportamiento esperado:
    - La ventana inicia la carrera al presionar Start. Los resultados aparecen solo al finalizar todos los hilos.
    - El ranking en consola no presenta posiciones duplicadas ni inconsistentes. El ganador corresponde a la posición 1.
    - Los botones Stop/Continue pausan y reanudan a todos los galgos correctamente.

Notas y verificación

- Se verificó la concurrencia ejecutando múltiples veces la Parte II: no se observaron posiciones repetidas ni cambios de ganador tras la sincronización.
- La UI permanece fluida porque el arranque de la carrera y la espera con `join()` suceden en un hilo en segundo plano (no en el EDT).


## Criterios de evaluación

1. Funcionalidad.

    1.1. La ejecución de los galgos puede ser detenida y resumida consistentemente.
    
    1.2. No hay inconsistencias en el orden de llegada registrado.
    
2. Diseño.   

    2.1. Se hace una sincronización de sólo la región crítica (sincronizar, por ejemplo, todo un método, bloquearía más de lo necesario).
    
    2.2. Los galgos, cuando están suspendidos, son reactivados son sólo un llamado (usando un monitor común).

