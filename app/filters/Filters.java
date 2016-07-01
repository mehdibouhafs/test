package filters;



import play.filters.cors.CORSFilter;
import play.http.DefaultHttpFilters;
import play.http.HttpFilters;
import play.mvc.EssentialFilter;
import scala.collection.Seq;

import javax.inject.Inject;

/**
 * Created by MBS on 01/07/2016.
 */
public class Filters implements HttpFilters {

    @Inject
    CORSFilter corsFilter;


    @Override
    public EssentialFilter[] filters() {
        return new EssentialFilter[] { corsFilter.asJava() };
    }
}
