package com.yougou.logistics.city.common.dto.baseinfo;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.NONE)
public class StoreDTO {
	
    private String storeNo;
    private String storeName;
    //表示该节点是否被选中
    private Boolean checked=false;
     //节点状态，'open' 或 'closed'，默认：'open'。在设置为'closed'的时候，当前节点的子节点将会从远程服务器加载他们
    private String state;
    
    @JsonProperty(value="id")
	public String getStoreNo() {
		return storeNo;
	}
	public void setStoreNo(String storeNo) {
		this.storeNo = storeNo;
	}
	@JsonProperty(value="text")
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
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
