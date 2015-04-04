import java.util.HashSet;

public class Page {
    private Integer id;
    private Double rank;

    private HashSet<Page> inNeighborSet;
    private HashSet<Page> outNeighborSet;

    public Page(Integer id){
        this.id = id;
        this.inNeighborSet = new HashSet<Page>();
        this.outNeighborSet = new HashSet<Page>();
    }

    public Integer getPageId(){
        return this.id;
    }

    public Double getRank() {
        return rank;
    }

    public void setRank(Double rank) {
        this.rank = rank;
    }

    public int getInDegree(){
        return inNeighborSet.size();
    }

    public void addInNeighbor(Page newNeighbor){
        this.inNeighborSet.add(newNeighbor);
    }

    public boolean testInNeighbor(Page testPage){
        return this.inNeighborSet.contains(testPage);
    }

    public Page[] getAllInNeighbors(){
        return this.inNeighborSet.toArray(new Page[inNeighborSet.size()]);
    }

    public int getOutDegree(){
        return outNeighborSet.size();
    }

    public void addOutNeighbor(Page newNeighbor){
        this.outNeighborSet.add(newNeighbor);
    }

    public boolean testOutNeighbor(Page testPage){
        return this.outNeighborSet.contains(testPage);
    }

    public Page[] getAllOutNeighbors(){
        return this.outNeighborSet.toArray(new Page[outNeighborSet.size()]);
    }
    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Page))
            return false;

        Page pageObj = (Page)obj;
        if(this.id == pageObj.id)
            return true;

        return false;
    }

}
