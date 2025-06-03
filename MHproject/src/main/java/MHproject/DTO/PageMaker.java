package MHproject.DTO;

public class PageMaker {
	
	private Criteria cri;
    private int totalCount;
    private int startPage;
    private int endPage;
    private boolean t_prev;
    private boolean t_next;
    private int displayPageNum = 10;
    
    public Criteria getCri() {
        return cri;
    }
    public void setCri(Criteria cri) {
        this.cri = cri;
    }
    public int getTotalCount() {
        return totalCount;
    }
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        calcData();
    }
    
    private void calcData() {
        
        endPage = (int) (Math.ceil(cri.getPage() / (double) displayPageNum) * displayPageNum);
 
        
        if(startPage <= 0) {
        	startPage = 1;
        
        }else {
        	startPage = (endPage - displayPageNum) + 1;
        }
        
        int tempEndPage = (int) (Math.ceil(totalCount / (double) cri.getPerPageNum()));
        if (endPage > tempEndPage) {
            endPage = tempEndPage;
        }
 
        t_prev = startPage == 1 ? false : true;
        t_next = endPage * cri.getPerPageNum() < totalCount ? true : false;
        
    }
    
    public int getStartPage() {
        return startPage;
    }
    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }
    public int getEndPage() {
        return endPage;
    }
    public void setEndPage(int endPage) {
        this.endPage = endPage;
    }
  
    public boolean isT_prev() {
		return t_prev;
	}
	public void setT_prev(boolean t_prev) {
		this.t_prev = t_prev;
	}
	public boolean isT_next() {
		return t_next;
	}
	public void setT_next(boolean t_next) {
		this.t_next = t_next;
	}
	public int getDisplayPageNum() {
        return displayPageNum;
    }
    public void setDisplayPageNum(int displayPageNum) {
        this.displayPageNum = displayPageNum;
    }
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PageMaker [cri=");
		builder.append(cri);
		builder.append(", totalCount=");
		builder.append(totalCount);
		builder.append(", startPage=");
		builder.append(startPage);
		builder.append(", endPage=");
		builder.append(endPage);
		builder.append(", t_prev=");
		builder.append(t_prev);
		builder.append(", t_next=");
		builder.append(t_next);
		builder.append(", displayPageNum=");
		builder.append(displayPageNum);
		builder.append("]");
		return builder.toString();
	}
    

}
