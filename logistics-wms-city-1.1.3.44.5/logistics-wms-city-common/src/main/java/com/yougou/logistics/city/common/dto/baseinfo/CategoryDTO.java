package com.yougou.logistics.city.common.dto.baseinfo;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.NONE)
public class CategoryDTO {
	
    private String cateNo;
    private String cateName;
    //表示该节点是否被选中
    private Boolean checked=false;
     //节点状态，'open' 或 'closed'，默认：'open'。在设置为'closed'的时候，当前节点的子节点将会从远程服务器加载他们
    private String state;
    
    @JsonProperty(value="id")
	public String getCateNo() {
		return cateNo;
	}
	public void setCateNo(String cateNo) {
		this.cateNo = cateNo;
	}
	
	@JsonProperty(value="text")
	public String getCateName() {
		return cateName;
	}
	public void setCateName(String cateName) {
		this.cateName = cateName;
	}
	public Boolean getChecked() {
		return checked;
	}
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
    
    

}
