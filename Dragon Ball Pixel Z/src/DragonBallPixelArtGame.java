import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

//Daniel Santiago Ostos Urrego - 192358
//Dorainy Guerrero Bayona - 192366
//Joan David Monroy Quintero 192374
//Daniel Adolfo Amaya Rodriguez 192381


class Sprite {
    String nombre;
    int x, y;
    int vida, vidaInicial; // Nueva propiedad para guardar la vida inicial
    int fuerza;
    int ancho, alto;
    int curacionesRestantes, curacionesIniciales; // Nueva propiedad para curaciones iniciales
    Image imagen;
    String tipoProyectil;
    private long ultimoAtaque;

    public Sprite(String nombre, int x, int y, int vida, int fuerza, int ancho, int alto, String rutaImagen, String tipoProyectil) {
        this.nombre = nombre;
        this.x = x;
        this.y = y;
        this.vida = this.vidaInicial = vida; // Guardar vida inicial
        this.fuerza = fuerza;
        this.ancho = ancho;
        this.alto = alto;
        this.curacionesRestantes = this.curacionesIniciales = 3; // Guardar curaciones iniciales
        this.imagen = new ImageIcon(rutaImagen).getImage();
        this.tipoProyectil = tipoProyectil;
    }



    public void reiniciar() {
        this.vida = this.vidaInicial;
        this.curacionesRestantes = this.curacionesIniciales;
    }


    public void mover(int dx, int dy, int limiteAncho, int limiteAlto) {
        // Calcular las nuevas coordenadas
        int nuevaX = x + dx;
        int nuevaY = y + dy;
    
        // Restringir dentro de los límites
        if (nuevaX >= 0 && nuevaX + ancho <= limiteAncho) {
            x = nuevaX;
        }
        if (nuevaY >= 0 && nuevaY + alto <= limiteAlto) {
            y = nuevaY;
        }
    }
    

    public boolean estaVivo() {
        return vida > 0;
    }

    public void curar() {
        if (curacionesRestantes > 0) {
            vida += 30; // Recupera 30 puntos de vida
            curacionesRestantes--;
        }
    }

     // Método para verificar si puede atacar
     public boolean puedeAtacar() {
        long ahora = System.currentTimeMillis();
        if (ahora - ultimoAtaque >= 190) { 
            ultimoAtaque = ahora; // Actualizar el tiempo del último ataque
            return true;
        }
        return false;
    }
}

class Proyectil {
    int x, y;
    int velocidadX, velocidadY;
    Sprite atacante;
    Image imagen;

    public Proyectil(int x, int y, int velocidadX, int velocidadY, Sprite atacante, String rutaImagen) {
        this.x = x;
        this.y = y;
        this.velocidadX = velocidadX;
        this.velocidadY = velocidadY;
        this.atacante = atacante;
        this.imagen = new ImageIcon(rutaImagen).getImage();
    }

    public void mover() {
        x += velocidadX;
        y += velocidadY;
    }

    public boolean colisionaCon(Sprite enemigo) {
        int centroProyectilX = x + 40; // Ajusta al centro del proyectil
        int centroProyectilY = y + 40;
        int centroEnemigoX = enemigo.x + enemigo.ancho / 2;
        int centroEnemigoY = enemigo.y + enemigo.alto / 2;
    
        return Math.abs(centroProyectilX - centroEnemigoX) < enemigo.ancho / 3 &&
               Math.abs(centroProyectilY - centroEnemigoY) < enemigo.alto / 3;
    }
    
}



public class DragonBallPixelArtGame extends JPanel implements KeyListener, ActionListener {
    private Sprite jugador1;
    private Sprite jugador2;
    private Image fondo;
    private Timer timer;
    private ArrayList<Proyectil> proyectiles;

    public DragonBallPixelArtGame(Sprite jugador1, Sprite jugador2) {
        setPreferredSize(new Dimension(1600, 1000));
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;

        // Posición inicial fija
        jugador1.x = 200;
        jugador1.y = 400;
        jugador2.x = 1100;
        jugador2.y = 400;


        // fondo del escenario 
        fondo = new ImageIcon("images/tournament.jpg").getImage();
        proyectiles = new ArrayList<>();
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(14, this); // 60 FPS
        timer.start();
    }



    private void reiniciarJuego() {
        jugador1.reiniciar(); // Restaurar el estado inicial del jugador 1
        jugador2.reiniciar(); // Restaurar el estado inicial del jugador 2
    
        jugador1.x = 200;
        jugador1.y = 400;
        jugador2.x = 1100;
        jugador2.y = 400;
    
        proyectiles.clear(); // Limpieza de proyectiles
        repaint(); // Actualizar la pantalla
    }
    
    

    @Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.drawImage(fondo, 0, 0, getWidth(), getHeight(), null);
    g.drawImage(jugador1.imagen, jugador1.x, jugador1.y, jugador1.ancho, jugador1.alto, null);
    g.drawImage(jugador2.imagen, jugador2.x, jugador2.y, jugador2.ancho, jugador2.alto, null);

    for (Proyectil proyectil : proyectiles) {
        g.drawImage(proyectil.imagen, proyectil.x, proyectil.y, 80, 90, null);
    }

    // Dibujar barras de vida estáticas debajo de la información
    g.setColor(Color.GREEN);
    g.fillRect(280, 60, jugador1.vida, 8); // Barra de vida del jugador 1
    g.fillRect(1100, 60, jugador2.vida, 8); // Barra de vida del jugador 2

    g.setColor(Color.WHITE);
    g.setFont(new Font("Arial", Font.BOLD, 22));
    g.drawString(jugador1.nombre + " - Vida: " + jugador1.vida + " (Curaciones: " + jugador1.curacionesRestantes + ")", 280, 30);
    g.drawString(jugador2.nombre + " - Vida: " + jugador2.vida + " (Curaciones: " + jugador2.curacionesRestantes + ")", 1100, 30);
}




   private void lanzarProyectil(Sprite atacante, int direccionX, int direccionY) {
    if (!atacante.puedeAtacar()) {
        return; // No hacer nada si no puede atacar aún
    }


    String rutaImagen = atacante.tipoProyectil.equals("kameha") ? "images/kameha.png" : "images/rayoos.png";
    Proyectil proyectil = new Proyectil(atacante.x + atacante.ancho / 2, atacante.y + atacante.alto / 2, direccionX * 15, direccionY * 15, atacante, rutaImagen);
    proyectiles.add(proyectil);
}

@Override
public void actionPerformed(ActionEvent e) {
    ArrayList<Proyectil> proyectilesAEliminar = new ArrayList<>();
    for (Proyectil proyectil : proyectiles) {
        proyectil.mover();
        if (proyectil.colisionaCon(jugador1) && proyectil.atacante != jugador1) {
            jugador1.vida -= proyectil.atacante.fuerza;
            proyectilesAEliminar.add(proyectil);
        } else if (proyectil.colisionaCon(jugador2) && proyectil.atacante != jugador2) {
            jugador2.vida -= proyectil.atacante.fuerza;
            proyectilesAEliminar.add(proyectil);
        }
    }
    proyectiles.removeAll(proyectilesAEliminar);

    if (!jugador1.estaVivo() || !jugador2.estaVivo()) {
        Sprite ganador = jugador1.estaVivo() ? jugador1 : jugador2;
        int opcion = JOptionPane.showOptionDialog(
            this,
            ganador.nombre + "  ha ganado!\n¿Qué deseas hacer?",
            "Juego Terminado",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            new String[]{"Jugar de nuevo", "Cambiar de personajes", "Salir del juego "},
            "Jugar de nuevo"
        );

        switch (opcion) {
            case 0 : reiniciarJuego();
                break;// Jugar de nuevo
            case 1 : {
                reiniciarSelector();
                reiniciarJuego(); // Cambiar personajes
                return; // Salir para evitar más procesamiento
            }
            case 2 : System.exit(0); // Salir del juego
        }
    }

    repaint();
}


private void reiniciarSelector() {
    JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
    frame.dispose(); // Cerrar la ventana actual del juego

    DragonBallPixelArtGame.main(new String[]{}); // Reiniciar desde el selector de personajes
}




 



    @Override
public void keyPressed(KeyEvent e) {
    // Obtener los límites del panel
    int limiteAncho = this.getWidth();
    int limiteAlto = this.getHeight();

    try {
        // Variables de dirección para determinar el movimiento
        boolean jugador1Arriba = false, jugador1Abajo = false, jugador1Izquierda = false, jugador1Derecha = false;
        boolean jugador2Arriba = false, jugador2Abajo = false, jugador2Izquierda = false, jugador2Derecha = false;

      // Verificar las teclas presionadas para ambos jugadores
      switch (e.getKeyCode()) {
        case KeyEvent.VK_W:
            jugador1.mover(0, -20, limiteAncho, limiteAlto);
            break;
        case KeyEvent.VK_S:
            jugador1.mover(0, 20, limiteAncho, limiteAlto);
            break;
        case KeyEvent.VK_A:
            jugador1.mover(-20, 0, limiteAncho, limiteAlto);
            break;
        case KeyEvent.VK_D:
            jugador1.mover(20, 0, limiteAncho, limiteAlto);
            break;
        case KeyEvent.VK_R:
            jugador1.curar();
            break;
        case KeyEvent.VK_SPACE:
            lanzarProyectil(jugador1, 1, 0);
            break;

        case KeyEvent.VK_UP:
            jugador2.mover(0, -20, limiteAncho, limiteAlto);
            break;
        case KeyEvent.VK_DOWN:
            jugador2.mover(0, 20, limiteAncho, limiteAlto);
            break;
        case KeyEvent.VK_LEFT:
            jugador2.mover(-20, 0, limiteAncho, limiteAlto);
            break;
        case KeyEvent.VK_RIGHT:
            jugador2.mover(20, 0, limiteAncho, limiteAlto);
            break;
        case KeyEvent.VK_SHIFT:
            jugador2.curar();
            break;
        case KeyEvent.VK_ENTER:
            lanzarProyectil(jugador2, -1, 0);
            break;

           
        }

        // Movimiento diagonal para jugador 1
        if (jugador1Arriba && jugador1Izquierda) jugador1.mover(-20, -20, limiteAncho, limiteAlto);
        else if (jugador1Arriba && jugador1Derecha) jugador1.mover(20, -20, limiteAncho, limiteAlto);
        else if (jugador1Abajo && jugador1Izquierda) jugador1.mover(-20, 20, limiteAncho, limiteAlto);
        else if (jugador1Abajo && jugador1Derecha) jugador1.mover(20, 20, limiteAncho, limiteAlto);
        else if (jugador1Arriba) jugador1.mover(0, -20, limiteAncho, limiteAlto);
        else if (jugador1Abajo) jugador1.mover(0, 20, limiteAncho, limiteAlto);
        else if (jugador1Izquierda) jugador1.mover(-20, 0, limiteAncho, limiteAlto);
        else if (jugador1Derecha) jugador1.mover(20, 0, limiteAncho, limiteAlto);

        // Movimiento diagonal para jugador 2
        if (jugador2Arriba && jugador2Izquierda) jugador2.mover(-20, -20, limiteAncho, limiteAlto);
        else if (jugador2Arriba && jugador2Derecha) jugador2.mover(20, -20, limiteAncho, limiteAlto);
        else if (jugador2Abajo && jugador2Izquierda) jugador2.mover(-20, 20, limiteAncho, limiteAlto);
        else if (jugador2Abajo && jugador2Derecha) jugador2.mover(20, 20, limiteAncho, limiteAlto);
        else if (jugador2Arriba) jugador2.mover(0, -20, limiteAncho, limiteAlto);
        else if (jugador2Abajo) jugador2.mover(0, 20, limiteAncho, limiteAlto);
        else if (jugador2Izquierda) jugador2.mover(-20, 0, limiteAncho, limiteAlto);
        else if (jugador2Derecha) jugador2.mover(20, 0, limiteAncho, limiteAlto);

    } catch (IllegalArgumentException ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de Tecla", JOptionPane.WARNING_MESSAGE);
    }
} 

    


    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    @SuppressWarnings("unused")
    private static int mostrarSelectorConImagenes(String mensaje, Sprite[] opciones) {
    JPanel panel = new JPanel(new GridLayout(0, opciones.length));
    
    // Botones para seleccionar directamente por clic
    JButton[] botones = new JButton[opciones.length];
    for (int i = 0; i < opciones.length; i++) {
        JPanel opcionPanel = new JPanel(new BorderLayout());
        JButton boton = new JButton(new ImageIcon(opciones[i].imagen.getScaledInstance(100, 150, Image.SCALE_SMOOTH)));
        boton.setActionCommand(String.valueOf(i)); // Asigna el índice como comando de acción
        botones[i] = boton;
        opcionPanel.add(boton, BorderLayout.CENTER);
        opcionPanel.add(new JLabel(opciones[i].nombre, JLabel.CENTER), BorderLayout.SOUTH);
        panel.add(opcionPanel);
    }

    // Crear botón adicional para cerrar el selector
    JButton botonCerrar = new JButton("SALIR DEL JUEGO ");
    botonCerrar.addActionListener(e -> System.exit(0)); // Cierra toda la aplicación

    // Crear cuadro de diálogo y configurar los botones
    final int[] seleccion = {-1}; // Para almacenar la selección del usuario
    JDialog dialog = new JDialog();
    dialog.setTitle(mensaje);
    dialog.setModal(true);
    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    
    // Listener para manejar clics en las imágenes
    ActionListener listener = e -> {
        seleccion[0] = Integer.parseInt(e.getActionCommand());
        dialog.dispose();
    };
    for (JButton boton : botones) {
        boton.addActionListener(listener);
    }

    // Agregar paneles y botón de cerrar al cuadro de diálogo
    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.add(panel, BorderLayout.CENTER);
    mainPanel.add(botonCerrar, BorderLayout.SOUTH);

    dialog.getContentPane().add(mainPanel);
    dialog.pack();
    dialog.setLocationRelativeTo(null);
    dialog.setVisible(true);

    return seleccion[0]; // Retorna la selección del usuario o -1 si no seleccionó
}





// ejecucion del juego 

static class MenuInicial {
    public static void mostrarMenu() {
        JFrame frameMenu = new JFrame("MENÚ DRAGON BALL PIXEL Z");
        frameMenu.setSize(1200, 900);
        frameMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameMenu.setLocationRelativeTo(null);
        frameMenu.setLayout(null);

        // Fondo del menú
        JLabel fondoMenu = new JLabel(new ImageIcon("images/menu.gif"));
        fondoMenu.setBounds(0, 0, 1200, 900);
        frameMenu.setContentPane(fondoMenu);
        fondoMenu.setLayout(null);

        // Botón de Iniciar Juego
        JButton btnIniciar = new JButton("Iniciar Juego");
        btnIniciar.setBounds(500, 300, 200, 50);
        btnIniciar.addActionListener(e -> {
            frameMenu.dispose();
            mostrarSelectorDePersonajes();
        });

        // Botón de Controles
        JButton btnControles = new JButton("Controles");
        btnControles.setBounds(500, 400, 200, 50);
        btnControles.addActionListener(e -> mostrarControles());

        // Botón de Salir
        JButton btnSalir = new JButton("Salir");
        btnSalir.setBounds(500, 500, 200, 50);
        btnSalir.addActionListener(e -> System.exit(0));

        // Añadir botones al fondo
        fondoMenu.add(btnIniciar);
        fondoMenu.add(btnControles);
        fondoMenu.add(btnSalir);

        frameMenu.setVisible(true);
    }


    private static void mostrarControles() {
        String mensaje = "CONTROLES:\n\n" +
                 "Jugador 1:\n" +
                 "- Movimiento: W (Arriba), A (Izquierda), S (Abajo), D (Derecha)\n" +
                 "- Atacar: Espacio\n" +
                 "- Curar: R\n\n" +
                 "Jugador 2:\n" +
                 "- Movimiento: Flecha Arriba (↑), Flecha Izquierda (←), Flecha Abajo (↓), Flecha Derecha (→)\n" +
                 "- Atacar: Enter\n" +
                 "- Curar: Shift\n";

        JOptionPane.showMessageDialog(null, mensaje, "Controles del Juego", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void mostrarSelectorDePersonajes() {
        boolean jugadoresSeleccionados = false;
        while (!jugadoresSeleccionados) {
            try {
                Sprite[] opciones = {
                    new Sprite("Goku", 100, 400, 120, 25, 130, 160, "images/goku.png", "kameha"),
                    new Sprite("Vegeta", 100, 400, 120, 25, 130, 150, "images/vegeta.png", "rayoos"),
                    new Sprite("Freezer", 100, 400, 130, 20, 120, 150, "images/freezer.png", "rayoos"),
                    new Sprite("Broly", 100, 400, 110, 30, 140, 170, "images/Broly.png", "rayoos"),
                    new Sprite("Gohan", 100, 400, 129, 22, 125, 170, "images/gohan.png", "kameha"),
                    new Sprite("Gogeta", 100, 400, 160, 27, 120, 170, "images/gogeta.gif", "kameha"),
                    new Sprite("Jiren", 100, 400, 155, 26, 110, 160, "images/jiren.png", "rayoos"),
                    new Sprite("Majin Buu", 100, 400, 125, 20, 110, 150, "images/majinbuu.png", "rayoos")
                };

                int seleccionJugador1 = mostrarSelectorConImagenes("Jugador 1 selecciona tu personaje", opciones);
                int seleccionJugador2 = mostrarSelectorConImagenes("Jugador 2 selecciona tu personaje", opciones);

                if (seleccionJugador1 == seleccionJugador2) {
                    JOptionPane.showMessageDialog(
                        null,
                        "Ambos jugadores no pueden elegir el mismo personaje. Inténtalo de nuevo.",
                        "Selección inválida",
                        JOptionPane.WARNING_MESSAGE
                    );
                    continue;
                }

                Sprite jugador1 = opciones[seleccionJugador1];
                Sprite jugador2 = opciones[seleccionJugador2];

                JFrame frame = new JFrame("Dragon Ball Pixel Art Game");
                DragonBallPixelArtGame game = new DragonBallPixelArtGame(jugador1, jugador2);

                frame.add(game);
                frame.pack();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

                jugadoresSeleccionados = true;

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error !!!", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        }
    }

    public static int mostrarSelectorConImagenes(String mensaje, Sprite[] opciones) {
        JPanel panel = new JPanel(new GridLayout(0, opciones.length));

        JButton[] botones = new JButton[opciones.length];
        for (int i = 0; i < opciones.length; i++) {
            JPanel opcionPanel = new JPanel(new BorderLayout());
            JButton boton = new JButton(new ImageIcon(opciones[i].imagen.getScaledInstance(100, 150, Image.SCALE_SMOOTH)));
            boton.setActionCommand(String.valueOf(i));
            botones[i] = boton;
            opcionPanel.add(boton, BorderLayout.CENTER);
            opcionPanel.add(new JLabel(opciones[i].nombre, JLabel.CENTER), BorderLayout.SOUTH);
            panel.add(opcionPanel);
        }

        JButton botonCerrar = new JButton("Salir");
        botonCerrar.addActionListener(e -> System.exit(0));

        final int[] seleccion = {-1};
        JDialog dialog = new JDialog();
        dialog.setTitle(mensaje);
        dialog.setModal(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        ActionListener listener = e -> {
            seleccion[0] = Integer.parseInt(e.getActionCommand());
            dialog.dispose();
        };
        for (JButton boton : botones) {
            boton.addActionListener(listener);
        }

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.add(botonCerrar, BorderLayout.SOUTH);

        dialog.getContentPane().add(mainPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

        return seleccion[0];
    }
}

    public static void main(String[] args) {

        
        MenuInicial.mostrarMenu(); // Mostrar el menú inicial y  ejcutar todo el juego xd 

        
    }
    

    
}
