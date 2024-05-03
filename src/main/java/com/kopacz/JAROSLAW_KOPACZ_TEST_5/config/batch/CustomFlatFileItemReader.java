package com.kopacz.JAROSLAW_KOPACZ_TEST_5.config.batch;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.exceptions.InvalidCsvException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.core.io.Resource;

import java.io.IOException;

public class CustomFlatFileItemReader<T> extends FlatFileItemReader<T> {

    private Resource resource;

    @Override
    public void setResource(Resource resource) {
        this.resource = resource;
        super.setResource(resource);
    }

    @Override
    public T read() {
        T item = null;
        try {
            item = super.read();
            if (item == null) {
                deleteFile(this.resource);
            }
            return item;
        } catch (Exception e) {
            throw new InvalidCsvException("Invalid csv");
        }
    }

    private void deleteFile(Resource resource) throws IOException {
        if (resource.exists()) {
            resource.getFile().delete();
        }
    }
}

