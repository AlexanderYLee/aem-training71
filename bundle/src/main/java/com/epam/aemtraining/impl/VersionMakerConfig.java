package com.epam.aemtraining.impl;

import com.epam.aemtraining.IVersionMakerConfig;
import org.apache.felix.scr.annotations.*;
import org.osgi.service.component.ComponentContext;

import java.util.Dictionary;


/**
 *
 * Created by Aliaksandr_Li on 5/13/2016.
 */
@Service(IVersionMakerConfig.class)
@Component(immediate = true, metatype = true)
public class VersionMakerConfig implements IVersionMakerConfig {
    private final static String PATH_PARAMETER_NAME="com.epam.aemtraining71.path";
    private final static String PATH_PARAMETER_DEFAULT="/content/myapp";

    @Property(name=PATH_PARAMETER_NAME, label = "Page event handler path", value = PATH_PARAMETER_DEFAULT)
    private String pathToHandle;

    @Activate
    @Modified
    protected void activate(ComponentContext ctx){
        Dictionary<String, Object> properties = ctx.getProperties();
        pathToHandle = (String)properties.get(PATH_PARAMETER_NAME);
    }
    public String getPathToHandle() {
        return pathToHandle;
    }
}
