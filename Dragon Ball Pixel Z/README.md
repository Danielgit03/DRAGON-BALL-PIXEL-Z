# Dragon Ball Pixel Art Game

## Descripcion:

Dragon Ball Pixel Art Game es un juego de lucha en 2D inspirado en el universo de Dragon Ball. Los jugadores seleccionan personajes y compiten en un combate con proyectiles y habilidades especiales. El juego cuenta con gráficos retro y controles intuitivos.

## Caracteristicas:

-Selección de personajes: Escoge entre varios personajes del universo de Dragon Ball.
-Sistema de combate: Lanza ataques, realiza curaciones estratégicas y esquiva movimientos enemigos.
-Interfaz gráfica: Ambiente visual con gráficos de pixel art y un fondo dinámico.
-Barras de vida: Muestra el estado de salud y curaciones restantes de cada jugador.
-Múltiples proyectiles: Ataques personalizados para cada personaje.
-Menú interactivo: Sistema de selección de personajes y reinicio de partidas.

**Menu:**




## Controles del juego:
1. Jugador 1
-Movimiento: W (arriba), A (izquierda), S (abajo), D (derecha)
-Ataque: Barra espaciadora (SPACE)
-Curar: R
2. Jugador 2
-Movimiento: Flechas direccionales (↑, ←, ↓, →)
-Ataque: ENTER
-Curar: SHIFT

## Estructura del juego:
1. **Clase Sprite:** Representa a los personajes. Contiene atributos como:
    Atributos:
    * nombre: Nombre del personaje.
    * x, y: Coordenadas de posición.
    * vida, vidaInicial: Puntos de vida actuales e iniciales.
    * fuerza: Daño que inflige al atacar.
    * ancho, alto: Dimensiones del sprite.
    * curacionesRestantes, curacionesIniciales: Curaciones disponibles y su cantidad inicial.
    * imagen: Imagen asociada al sprite.
    * tipoProyectil: Tipo de proyectil que puede disparar.
    * ultimoAtaque: Momento del último ataque, usado para calcular si puede atacar de nuevo.
    * 
    Métodos:
    * Sprite(...): Constructor que inicializa los atributos.
    * mover(dx, dy, limiteAncho, limiteAlto): Permite mover el sprite dentro de los límites del panel.
    * estaVivo(): Retorna si el sprite tiene vida restante.
    * curar(): Recupera puntos de vida si tiene curaciones disponibles.
    * puedeAtacar(): Verifica si puede realizar un ataque basándose en el tiempo transcurrido.
    * reiniciar(): Restaura los atributos al estado inicial.
    

2. **Clase Proyectil:** Representa los ataques de los personajes.

    Atributos:
    * x, y: Posición actual del proyectil.
    * velocidadX, velocidadY: Dirección y velocidad del proyectil.
    * atacante: Referencia al sprite que disparó el proyectil.
    * imagen: Imagen asociada al proyectil.
    Métodos:
    * Proyectil(...): Constructor que inicializa los atributos.
    * mover(): Actualiza la posición del proyectil según su velocidad.
    * colisionaCon(Sprite enemigo): Verifica si el proyectil ha colisionado con un sprite enemigo.
    
3. **Clase DragonBallPixelArtGame:** Clase principal que gestiona el juego.
   Atributos:
   * jugador1, jugador2: Referencias a los sprites de los dos jugadores.
   * fondo: Imagen de fondo del juego.
   * timer: Temporizador para manejar el flujo del juego.
   * proyectiles: Lista de proyectiles activos en el juego.
   Métodos:
   * DragonBallPixelArtGame(...): Constructor que inicializa el panel del juego y configura los jugadores.
   * paintComponent(Graphics g): Dibuja el fondo, los sprites y los proyectiles en pantalla.
   * lanzarProyectil(atacante, direccionX, direccionY): Crea y dispara un proyectil desde un sprite.
   * actionPerformed(ActionEvent e): Lógica principal del juego, que mueve proyectiles, verifica colisiones y actualiza el estado de los jugadores.
   * reiniciarJuego(): Restaura el estado inicial del juego.
   * keyPressed(KeyEvent e): Maneja las entradas del teclado para mover a los jugadores y realizar acciones como disparar o curarse.
   


4. **Clase MenuInicial:** Crea el menú principal y el sistema de selección de personajes.
   * Métodos:
   * mostrarMenu(): Muestra la ventana del menú principal con opciones para iniciar el juego o salir.
   * mostrarSelectorDePersonajes(): Presenta una interfaz para que los jugadores seleccionen sus personajes.


## Funcionalidades Clave

1. **Selector de Personajes:**
   *  Permite a los jugadores elegir entre diferentes personajes utilizando botones con imágenes.

2. **Barra de Vida y Curaciones:** 
   * Cada personaje tiene una barra de vida que se reduce al recibir daño.
   * El número de curaciones restantes se muestra junto a la barra.

3. **Sistema de Proyectiles:**
   * Los ataques tienen diferentes sprites y velocidades.
   * Los proyectiles detectan colisiones con el enemigo y reducen su vida.

4. **Reinicio del Juego:**
   * Cuando un jugador gana, se ofrece la opción de:
       -Jugar de nuevo.
       -Cambiar de personajes.
       -Salir del juego.


## Autores
Daniel Santiago Ostos Urrego - ID: 192358
Dorainy Guerrero Bayona - ID: 192366
Joan David Monroy Quintero - ID: 192374
Daniel Adolfo Amaya Rodríguez - ID: 192381















