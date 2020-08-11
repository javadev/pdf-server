package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Pdffile;
import com.mycompany.myapp.repository.PdffileRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Pdffile}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PdffileResource {

    private final Logger log = LoggerFactory.getLogger(PdffileResource.class);

    private static final String ENTITY_NAME = "pdffile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PdffileRepository pdffileRepository;

    public PdffileResource(PdffileRepository pdffileRepository) {
        this.pdffileRepository = pdffileRepository;
    }

    /**
     * {@code POST  /pdffiles} : Create a new pdffile.
     *
     * @param pdffile the pdffile to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pdffile, or with status {@code 400 (Bad Request)} if the pdffile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pdffiles")
    public ResponseEntity<Pdffile> createPdffile(@Valid @RequestBody Pdffile pdffile) throws URISyntaxException {
        log.debug("REST request to save Pdffile : {}", pdffile);
        if (pdffile.getId() != null) {
            throw new BadRequestAlertException("A new pdffile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Pdffile result = pdffileRepository.save(pdffile);
        return ResponseEntity.created(new URI("/api/pdffiles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pdffiles} : Updates an existing pdffile.
     *
     * @param pdffile the pdffile to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pdffile,
     * or with status {@code 400 (Bad Request)} if the pdffile is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pdffile couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pdffiles")
    public ResponseEntity<Pdffile> updatePdffile(@Valid @RequestBody Pdffile pdffile) throws URISyntaxException {
        log.debug("REST request to update Pdffile : {}", pdffile);
        if (pdffile.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Pdffile result = pdffileRepository.save(pdffile);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pdffile.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /pdffiles} : get all the pdffiles.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pdffiles in body.
     */
    @GetMapping("/pdffiles")
    public ResponseEntity<List<Pdffile>> getAllPdffiles(Pageable pageable) {
        log.debug("REST request to get a page of Pdffiles");
        Page<Pdffile> page = pdffileRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pdffiles/:id} : get the "id" pdffile.
     *
     * @param id the id of the pdffile to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pdffile, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pdffiles/{id}")
    public ResponseEntity<Pdffile> getPdffile(@PathVariable Long id) {
        log.debug("REST request to get Pdffile : {}", id);
        Optional<Pdffile> pdffile = pdffileRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(pdffile);
    }

    /**
     * {@code DELETE  /pdffiles/:id} : delete the "id" pdffile.
     *
     * @param id the id of the pdffile to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pdffiles/{id}")
    public ResponseEntity<Void> deletePdffile(@PathVariable Long id) {
        log.debug("REST request to delete Pdffile : {}", id);
        pdffileRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
