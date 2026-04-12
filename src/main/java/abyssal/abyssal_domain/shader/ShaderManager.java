package abyssal.abyssal_domain.shader;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ShaderManager {
    private static final Logger LOGGER = LoggerFactory.getLogger("AbyssalDomainShader");
    
    private static final Map<String, ShaderConfig> registeredShaders = new HashMap<>();
    private static final Map<String, String> shaderAssignments = new HashMap<>();
    
    private static boolean initialized = false;
    
    public static void init() {
        if (initialized) return;
        LOGGER.info("Initializing Abyssal Domain Shader System");
        initialized = true;
    }
    
    public static void registerShader(String name, Identifier jsonLocation) {
        registeredShaders.put(name, new ShaderConfig(name, jsonLocation));
        LOGGER.info("Registered shader: " + name + " from " + jsonLocation);
    }
    
    public static void registerShader(String name, Identifier vshLocation, Identifier fshLocation) {
        registeredShaders.put(name, new ShaderConfig(name, vshLocation, fshLocation));
        LOGGER.info("Registered custom shader: " + name);
    }
    
    public static void registerBuiltInShader(String name) {
        registeredShaders.put(name, new ShaderConfig(name, null, null));
        LOGGER.info("Registered built-in shader: " + name);
    }
    
    public static void assignShader(String target, String shaderName) {
        if (!registeredShaders.containsKey(shaderName)) {
            LOGGER.warn("Cannot assign unregistered shader: " + shaderName);
            return;
        }
        
        shaderAssignments.put(target, shaderName);
        LOGGER.info("Assigned shader '" + shaderName + "' to '" + target + "'");
    }
    
    public static void unassignShader(String target) {
        String removed = shaderAssignments.remove(target);
        if (removed != null) {
            LOGGER.info("Unassigned shader from: " + target);
        }
    }
    
    public static String getAssignedShaderName(String target) {
        return shaderAssignments.get(target);
    }
    
    public static ShaderConfig getShaderConfig(String name) {
        return registeredShaders.get(name);
    }
    
    public static boolean isShaderRegistered(String name) {
        return registeredShaders.containsKey(name);
    }
    
    public static boolean isShaderAssigned(String target) {
        return shaderAssignments.containsKey(target);
    }
    
    public static List<String> getRegisteredShaders() {
        return new ArrayList<>(registeredShaders.keySet());
    }
    
    public static List<String> getAssignedTargets() {
        return new ArrayList<>(shaderAssignments.keySet());
    }
    
    public static void clearAllAssignments() {
        shaderAssignments.clear();
        LOGGER.info("Cleared all shader assignments");
    }
    
    public static void clearAll() {
        registeredShaders.clear();
        shaderAssignments.clear();
        LOGGER.info("Cleared all shader registrations");
    }
    
    public static class ShaderConfig {
        public final String name;
        public final Identifier jsonLocation;
        public final Identifier vshLocation;
        public final Identifier fshLocation;
        
        public ShaderConfig(String name, Identifier jsonLocation) {
            this.name = name;
            this.jsonLocation = jsonLocation;
            this.vshLocation = null;
            this.fshLocation = null;
        }
        
        public ShaderConfig(String name, Identifier vshLocation, Identifier fshLocation) {
            this.name = name;
            this.jsonLocation = null;
            this.vshLocation = vshLocation;
            this.fshLocation = fshLocation;
        }
        
        public Identifier getLocation() {
            if (jsonLocation != null) return jsonLocation;
            if (vshLocation != null) return vshLocation;
            return null;
        }
        
        public boolean isJsonFormat() {
            return jsonLocation != null;
        }
        
        public boolean isCustomFormat() {
            return vshLocation != null && fshLocation != null;
        }
    }
}
