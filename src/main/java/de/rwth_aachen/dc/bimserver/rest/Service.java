package de.rwth_aachen.dc.bimserver.rest;

import java.util.HashSet;
import java.util.Set;

public class Service {

	private String id=""+System.currentTimeMillis();
	private String name;
	private String description;
	private String provider;
	private String providerIcon;
	private Set<String> inputs = new HashSet<>();
	private Set<String> outputs = new HashSet<>();
	private String resourceUrl;
	private String authorizationUrl;
	private String registerUrl;
	
	
	
	public Service(long id,String name, String description, String provider, String providerIcon, String resourceUrl, String authorizationUrl, String registerUrl) {
		super();
		this.id=""+id;
		this.name = name;
		this.description = description;
		this.provider = provider;
		this.providerIcon = providerIcon;
		this.resourceUrl = resourceUrl;
		this.authorizationUrl = authorizationUrl;
		this.registerUrl = registerUrl;
	    this.inputs.add("application/ifc");	
	    this.outputs.add("application/ld+json");	
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public String getProviderIcon() {
		return providerIcon;
	}
	public void setProviderIcon(String providerIcon) {
		this.providerIcon = providerIcon;
	}
	public Set<String> getInputs() {
		return inputs;
	}
	public void setInputs(Set<String> inputs) {
		this.inputs = inputs;
	}
	public Set<String> getOutputs() {
		return outputs;
	}
	public void setOutputs(Set<String> outputs) {
		this.outputs = outputs;
	}
	public String getResourceUrl() {
		return resourceUrl;
	}
	public void setResourceUrl(String resourceUrl) {
		this.resourceUrl = resourceUrl;
	}
	public String getAuthorizationUrl() {
		return authorizationUrl;
	}
	public void setAuthorizationUrl(String authorizationUrl) {
		this.authorizationUrl = authorizationUrl;
	}
	public String getRegisterUrl() {
		return registerUrl;
	}
	public void setRegisterUrl(String registerUrl) {
		this.registerUrl = registerUrl;
	}
	

}
	