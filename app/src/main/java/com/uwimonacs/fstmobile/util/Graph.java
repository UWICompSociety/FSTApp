package com.uwimonacs.fstmobile.util;

/**
 * Created by Akinyele on 2/10/2017.
 * This represents the graph object with takes a list of vertex and edges.
 */


import com.uwimonacs.fstmobile.models.locations.Edge;
import com.uwimonacs.fstmobile.models.locations.Vertex;

import java.util.List;

public class Graph {
    private final List<Vertex> vertexes;
    private final List<Edge> edges;

    public Graph(List<Vertex> vertexes, List<Edge> edges) {
        this.vertexes = vertexes;
        this.edges = edges;
    }

    public List<Vertex> getVertexes() {
        return vertexes;
    }

    public List<Edge> getEdges() {
        return edges;
    }



}
