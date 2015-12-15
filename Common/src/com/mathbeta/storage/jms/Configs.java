package com.mathbeta.storage.jms;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "configs")
@XmlAccessorType(XmlAccessType.FIELD)
public class Configs {
	@XmlElement(name = "selector")
	private String selector;
	@XmlElement(name = "config")
	private List<Config> configs;

	public String getSelector() {
		return selector;
	}

	public void setSelector(String selector) {
		this.selector = selector;
	}

	public List<Config> getConfigs() {
		return configs;
	}

	public void setConfigs(List<Config> configs) {
		this.configs = configs;
	}
}
