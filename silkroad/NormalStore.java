package silkroad;

/**
 * Tienda normal sin efectos especiales.
 * Se comporta igual que la versión original.
 */
public class NormalStore extends Store {

    /**
     * Crea una tienda que se comporta e interactia normalmente con su entorno
     * @param posicion de la tienda
     * @param tenges de la tienda
     * @param carretera
     */
    public NormalStore(int location, int tenges, Road road) {
        super(location, tenges, road);
        this.color = "yellow";
        this.originalColor = color;
        createShape(road);
    }
}
