package com.mycompany.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class PdffileTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pdffile.class);
        Pdffile pdffile1 = new Pdffile();
        pdffile1.setId(1L);
        Pdffile pdffile2 = new Pdffile();
        pdffile2.setId(pdffile1.getId());
        assertThat(pdffile1).isEqualTo(pdffile2);
        pdffile2.setId(2L);
        assertThat(pdffile1).isNotEqualTo(pdffile2);
        pdffile1.setId(null);
        assertThat(pdffile1).isNotEqualTo(pdffile2);
    }
}
