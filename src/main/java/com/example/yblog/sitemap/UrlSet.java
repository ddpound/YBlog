package com.example.yblog.sitemap;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "urlset")
@XmlAccessorType(XmlAccessType.FIELD)
public class UrlSet {

    @XmlAttribute(name = "xmlns")
    private String xmlns;

    @XmlElement(name = "url")
    private List<Url> url;



}
