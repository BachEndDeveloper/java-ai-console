package com.example.plugins;

import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.microsoft.semantickernel.semanticfunctions.annotations.KernelFunctionParameter;

/**
 * A simple plugin for controlling smart home lights.
 * This plugin provides basic functionality to turn lights on and off in different locations.
 */
public class LightPlugin {

    /**
     * Turns on a light in the specified location.
     * 
     * @param location The location where the light should be turned on (e.g., "living room", "bedroom")
     * @return A confirmation message indicating the light has been turned on
     */
    @DefineKernelFunction(name = "TurnOnLight", description = "Turns on the light in a specific location")
    public String turnOnLight(
            @KernelFunctionParameter(name = "location", description = "The location of the light to turn on") String location) {
        return String.format("The light in the %s has been turned ON.", location);
    }

    /**
     * Turns off a light in the specified location.
     * 
     * @param location The location where the light should be turned off (e.g., "living room", "bedroom")
     * @return A confirmation message indicating the light has been turned off
     */
    @DefineKernelFunction(name = "TurnOffLight", description = "Turns off the light in a specific location")
    public String turnOffLight(
            @KernelFunctionParameter(name = "location", description = "The location of the light to turn off") String location) {
        return String.format("The light in the %s has been turned OFF.", location);
    }

    @DefineKernelFunction(name = "GetLightColors", description = "Gets the color of the light in a specific location")
    public String getLightColors(
            @KernelFunctionParameter(name = "location", description = "The location of the light to get colors from") String location) {
        // For demonstration purposes, return a fixed set of colors
        return String.format("The light colors in the %s are Red, Green, Blue.", location);
    }
}