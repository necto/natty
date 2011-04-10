/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.persist;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author zayac
 */
@Entity
@Table(name = "track")
@NamedQueries({
    @NamedQuery(name = "Track.findAll", query = "SELECT t FROM Track t"),
    @NamedQuery(name = "Track.findById", query = "SELECT t FROM Track t WHERE t.id = :id"),
    @NamedQuery(name = "Track.findByName", query = "SELECT t FROM Track t WHERE t.name = :name"),
    @NamedQuery(name = "Track.findByYear", query = "SELECT t FROM Track t WHERE t.year = :year"),
    @NamedQuery(name = "Track.findByUrl", query = "SELECT t FROM Track t WHERE t.url = :url")})
public class Track implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
	@SequenceGenerator(name="track_id_seq", sequenceName="track_id_seq")
	@GeneratedValue(generator="track_id_seq",strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "year")
    @Temporal(TemporalType.DATE)
    private Date year;
    @Column(name = "url")
    private String url;
    @ManyToMany(mappedBy="trackCollection")
    private Collection<Album> albumCollection;
    @ManyToMany(mappedBy="trackCollection")
    private Collection<Genre> genreCollection;
    @ManyToMany(mappedBy="trackCollection")
    private Collection<Artist> artistCollection;

    public Track() {
        albumCollection = new HashSet<Album>();
        artistCollection = new HashSet<Artist>();
        genreCollection = new HashSet<Genre>();
    }

    public Track(String name) {
        this();
        this.name = name.replaceAll("\u0000", "");
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.replaceAll("\u0000", "");
    }

    public Date getYear() {
        return year;
    }

    public void setYear(Date year) {
        this.year = year;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url.replaceAll("\u0000", "");
    }

    public Collection<Album> getAlbumCollection() {
        return albumCollection;
    }

    public void setAlbumCollection(Collection<Album> albumCollection) {
        this.albumCollection = albumCollection;
    }

    public Collection<Genre> getGenreCollection() {
        return genreCollection;
    }

    public void setGenreCollection(Collection<Genre> genreCollection) {
        this.genreCollection = genreCollection;
    }

    public Collection<Artist> getArtistCollection() {
        return artistCollection;
    }

    public void setArtistCollection(Collection<Artist> artistCollection) {
        this.artistCollection = artistCollection;
    }

    @Override
    public String toString() {
        return "ru.natty.persist.Track[id=" + id + "]";
    }

}

