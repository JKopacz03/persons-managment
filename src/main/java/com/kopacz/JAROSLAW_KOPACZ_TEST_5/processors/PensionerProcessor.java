package com.kopacz.JAROSLAW_KOPACZ_TEST_5.processors;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.Pensioner;
import org.springframework.batch.item.ItemProcessor;

public class PensionerProcessor implements ItemProcessor<Pensioner, Pensioner> {

    @Override
    public Pensioner process(Pensioner pensioner) throws Exception {
        return pensioner;
    }
}
