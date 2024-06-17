package prole.afd;

import java.awt.*;
import java.util.Map;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

/**
 * Convierte un AFD en un grafo dirigido que puede visualizarse mediante la
 * librería de licencia gratuita JUNG.
 *
 * @author Diego Francisco Darias Pino
 */
public class GrafoAFD {

    /**
     * Crea el grafo dirigido del AFD.
     *
     * @param afd AFD leído.
     * @return Grafo dirigido resultante.
     */
    public static VisualizationViewer<String, String> crear(AFD afd) {
        //Crea un grafo dirigido vacio
        DirectedSparseGraph<String, String> grafo = new DirectedSparseGraph<>();

        //Añade los vertices
        //Vertice ficticio que representa el punto de entrada del AFD
        grafo.addVertex("inicio");
        //Resto de vertices/Estado
        for (Map.Entry<String, Estado> entry : afd.tablaS.entrySet()) {
            grafo.addVertex(entry.getKey());
        }

        //Añade las aristas
        //Arista ficticia que representa la flecha de entrada del AFD
        grafo.addEdge("", "inicio", afd.inicial.nombre, EdgeType.DIRECTED);
        //Resto de aristas/Transicion
        for (Map.Entry<String, Transicion> entry : afd.tablaT.entrySet()) {
            Estado ori = entry.getValue().origen;
            Estado dest = entry.getValue().destino;
            grafo.addEdge(entry.getValue().toString(), ori.nombre, dest.nombre, EdgeType.DIRECTED);
        }

        //Crea los renderers del grafo
        VisualizationViewer<String, String> vv = new VisualizationViewer<>(new CircleLayout(grafo));
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());

        //Caracteristicas de los vertices
        //Posicion
        vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
        //Forma y Tamaño
        vv.getRenderContext().setVertexShapeTransformer(vertex -> {
            Estado s = afd.tablaS.get(vertex);
            if (s != null && afd.esFinal(s)) {
                //Vertices finales cuadrados
                return new java.awt.geom.Rectangle2D.Double(-10, -10, 40, 40);
            } else {
                //Resto de vertices circulares
                return new java.awt.geom.Ellipse2D.Double(-10, -10, 40, 40);
            }
        });
        //Color
        vv.getRenderContext().setVertexFillPaintTransformer(vertex -> {
            //Vertice ficticio blanco
            if (vertex.equals("inicio")) {
                return Color.WHITE;
                //Vertice actual verde
            } else if (afd.verde.nombre.equals(vertex)) {
                return Color.GREEN;
                //Resto de vertices azules
            } else {
                return Color.CYAN;
            }
        });

        //Devuelve el grafo creado
        return vv;
    }

}
