package prole.afd;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import java.awt.*;
import java.util.Map;

/**
 * Convierte un AFD en un grafo dirigido que puede visualizarse mediante la
 * librería JUNG.
 *
 * @author Diego Francisco Darias Pino
 */
public class GrafoAFD {

    /**
     * Crea el grafo dirigido.
     *
     * @param afd Autómata que es leído.
     * @return Grafo dirigido resultante.
     */
    public static VisualizationViewer<String, String> crear(AFD afd) {
        //crear un grafo dirigido vacío
        DirectedSparseGraph<String, String> grafo = new DirectedSparseGraph<>();

        //añadir los vértices
        grafo.addVertex("inicio");
        for (Map.Entry<String, Estado> entry : afd.tablaS.entrySet()) {
            grafo.addVertex(entry.getKey());
        }

        //añadir las aristas
        grafo.addEdge("", "inicio", afd.inicial.nombre, EdgeType.DIRECTED);
        //todas las transiciones
        for (Map.Entry<String, Transicion> entry : afd.tablaT.entrySet()) {
            Estado ori = entry.getValue().origen;
            Estado dest = entry.getValue().destino;
            grafo.addEdge(entry.getValue().toString(), ori.nombre, dest.nombre, EdgeType.DIRECTED);
        }

        //crear los visualizadores
        VisualizationViewer<String, String> vv = new VisualizationViewer<>(new CircleLayout(grafo));
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());

        //características de los vértices
        //posición
        vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
        //forma y tamaño
        vv.getRenderContext().setVertexShapeTransformer(vertex -> {
            Estado s = afd.tablaS.get(vertex);
            if (s != null && afd.esFinal(s)) {
                return new java.awt.geom.Rectangle2D.Double(-10, -10, 40, 40);
            } else {
                return new java.awt.geom.Ellipse2D.Double(-10, -10, 40, 40);
            }
        });
        //color
        vv.getRenderContext().setVertexFillPaintTransformer(vertex -> {
            if (vertex.equals("inicio")) {
                return Color.WHITE; //estado inicial en blanco
            } else if (afd.verde.nombre.equals(vertex)) {
                return Color.GREEN; //estado actual en verde
            } else {
                return Color.CYAN; //estados en cyan
            }
        });

        //devolver el grafo
        return vv;
    }

}
