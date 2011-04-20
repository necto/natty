/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.persist;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Query;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author zayac
 */
@Entity
@Table(name = "artist")
@NamedQueries({
    @NamedQuery(name = "Artist.findAll", query = "SELECT a FROM Artist a"),
    @NamedQuery(name = "Artist.findById", query = "SELECT a FROM Artist a WHERE a.id = :id"),
    @NamedQuery(name = "Artist.findByPattern", query = "SELECT a FROM Artist a WHERE UPPER(a.name) like UPPER(:name)")})
public class Artist implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
	@SequenceGenerator(name="artist_id_seq", sequenceName="artist_id_seq")
	@GeneratedValue(generator="artist_id_seq",strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @JoinTable(name = "tracks_artists", joinColumns = {
        @JoinColumn(name = "artist_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "track_id", referencedColumnName = "id")})
    @ManyToMany
    private Collection<Track> trackCollection;
    @JoinTable(name = "artists_genres", joinColumns = {
        @JoinColumn(name = "artist_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "genre_id", referencedColumnName = "id")})
    @ManyToMany
    private Collection<Genre> genreCollection;

    public Artist() {
        trackCollection = new HashSet<Track>();
        genreCollection = new HashSet<Genre>();  
    }

    public Artist(String name) {
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

    public Collection<Track> getTrackCollection() {
        return trackCollection;
    }

    public void setTrackCollection(Collection<Track> trackCollection) {
        this.trackCollection = trackCollection;
    }

    public Collection<Genre> getGenreCollection() {
        return genreCollection;
    }

    public void setGenreCollection(Collection<Genre> genreCollection) {
        this.genreCollection = genreCollection;
    }

    public static List<Artist> queryByPattern (String pattern, EntityManager em)
    {
		Query getGenres = em.createNamedQuery ("Artist.findByPattern");
		getGenres.setParameter("name", pattern);
		List rez = getGenres.getResultList();
		List<Artist> arts = new ArrayList<Artist>();

		for (Object o : rez)
			arts.add ((Artist)o);
		return arts;
    }

    @Override
    public String toString() {
        return "ru.natty.persist.Artist[id=" + id + "]";
    }

}

