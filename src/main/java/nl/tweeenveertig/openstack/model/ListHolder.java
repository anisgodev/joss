package nl.tweeenveertig.openstack.model;

import java.util.Collection;

public interface ListHolder<Child extends ListSubject> {

    /**
    * Returns all the containers in an Account. Note that this method returns a maxmimum of 9,999
    * Containers and no more (see http://docs.openstack.org/api/openstack-object-storage/1.0/content/list-objects.html)
    * @return the containers in an Account
    */
    public Collection<Child> list();

    /**
    * Returns a number equal to pageSize of Container elements, starting with the first element
    * after the Container named the same as marker.
    * @param marker the last element on the previous page
    * @param pageSize the number of elements to return
    * @return page of Containers in an Account with a total of pageSize elements
    */
    public Collection<Child> list(String marker, int pageSize);

    /**
    * Returns a number equal to pageSize of Container elements, starting with the first element
    * after the Container named the same as marker.
    * @param paginationMap the map that is fetched with getPaginationMap and maps from page to marker
    * @param page the page to return the Containers for
    * @return page of Containers in an Account with a total of pageSize elements
    */
    public Collection<Child> list(PaginationMap paginationMap, int page);

    /**
    * Returns a PaginationMap of a listing of Containers. The map can be used to supply the OpenStack
    * API with a marker (ie, last record on the previous page) and a limit (ie, page size).
    * BE AWARE: this method iterates over ALL Container names to draw up the map, therefore it must
    * be considered an expensive call.
    * @param pageSize number of elements on a single page
    * @return the pagination map for all the Container elements in Account
    */
    public PaginationMap getPaginationMap(int pageSize);

    public int getCount();

    public int getMaxPageSize();

}
