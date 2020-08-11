package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Pdffile;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Pdffile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PdffileRepository extends JpaRepository<Pdffile, Long> {

    @Query("select pdffile from Pdffile pdffile where pdffile.user.login = ?#{principal.username}")
    List<Pdffile> findByUserIsCurrentUser();

}
