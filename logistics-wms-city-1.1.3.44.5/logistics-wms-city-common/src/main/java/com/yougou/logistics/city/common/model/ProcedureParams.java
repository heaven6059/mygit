package com.yougou.logistics.city.common.model;

import java.util.List;

public class ProcedureParams {
	
	private List<SqlParam> inParams;
	
	private List<SqlParam> outParams;

	public List<SqlParam> getInParams() {
		return inParams;
	}

	public void setInParams(List<SqlParam> inParams) {
		this.inParams = inParams;
	}

	public List<SqlParam> getOutParams() {
		return outParams;
	}

	public void setOutParams(List<SqlParam> outParams) {
		this.outParams = outParams;
	}
	
	


	public class SqlParam{
		private Object content;
		private String type;
		public Object getContent() {
			return content;
		}
		public void setContent(Object content) {
			this.content = content;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
	}
}
