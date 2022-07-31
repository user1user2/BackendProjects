package com.servicenow.demoservice.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Result implements Serializable {

	
	
	@JsonProperty("number")
	private String number;
	@JsonProperty("category")
	private Object category;
	@JsonProperty("upon_approval")
	private Object upon_approval;
	@JsonProperty("caller_id")
	private Object caller_id;
	@JsonProperty("short_description")
	private String short_description;
	@JsonProperty("sys_id")
	private String sys_id;
	@JsonProperty("sys_created_by")
	private String sys_created_by;
	@JsonProperty("sys_mod_count")
	private String sys_mod_count;
	@JsonProperty("approval")
	private String approval;
	@JsonProperty("severity")
	private String severity;
	@JsonProperty("reassignment_count")
	private String reassignment_count;
	@JsonProperty("problem_id")
	private Object problem_id;
	@JsonProperty("incident_state")
	private String incident_state;
	@JsonProperty("contact_type")
	private String contact_type;
	@JsonProperty("sys_class_name")
	private String sys_class_name;
	@JsonProperty("close_notes")
	private String close_notes;
	
	
}