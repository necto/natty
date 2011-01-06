/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.persist;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;


/**
 *
 * @author necto
 */
@Entity
@Table(name = "track")
@NamedQueries(
{
    @NamedQuery(name = "Track.findAll", query = "SELECT t FROM Track t"),
    @NamedQuery(name = "Track.findById", query = "SELECT t FROM Track t WHERE t.id = :id"),
    @NamedQuery(name = "Track.findByName", query = "SELECT t FROM Track t WHERE t.name = :name"),
    @NamedQuery(name = "Track.findByYear", query = "SELECT t FROM Track t WHERE t.year = :year"),
    @NamedQuery(name = "Track.findByUrl", query = "SELECT t FROM Track t WHERE t.url = :url")
})
public class Track implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    @GeneratedValue
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Column(name = "year")
    @Temporal(TemporalType.DATE)
    private Date year;
    @Column(name = "url")
    private String url;
    @JoinColumn(name = "genre", referencedColumnName = "id")
    @ManyToOne
    private Genre genre;
    @JoinColumn(name = "artist", referencedColumnName = "id")
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Artist artist;
    @JoinColumn(name = "album", referencedColumnName = "id")
    @ManyToOne
    private Album album;

    public Track ()
    {
    }

    public Track (Integer id)
    {
        this.id = id;
    }

    public Track (Integer id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public Track (String name)
    {
        this.name = name;
    }

    public Integer getId ()
    {
        return id;
    }

    public void setId (Integer id)
    {
        this.id = id;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public Date getYear ()
    {
        return year;
    }

    public void setYear (Date year)
    {
        this.year = year;
    }

    public String getUrl ()
    {
        return url;
    }

    public void setUrl (String url)
    {
        this.url = url;
    }

    public Genre getGenre ()
    {
        return genre;
    }

    public void setGenre (Genre genre)
    {
        this.genre = genre;
    }

    public Artist getArtist ()
    {
        return artist;
    }

    public void setArtist (Artist artist)
    {
        this.artist = artist;
    }

    public Album getAlbum ()
    {
        return album;
    }

    public void setAlbum (Album album)
    {
        this.album = album;
    }

    @Override
    public int hashCode ()
    {
        int hash = 0;
        hash += (id != null ? id.hashCode () : 0);
        return hash;
    }

    @Override
    public boolean equals (Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Track))
        {
            return false;
        }
        Track other = (Track) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals (other.id)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString ()
    {
        return "ru.natty.persist.Track[id=" + id + "]";
    }

}