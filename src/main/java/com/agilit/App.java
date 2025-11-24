package com.agilit;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/api")
public class App extends Application {
    
    // Automaticamente o JAX-RS vai registrar todos os endpoints @Path
}
