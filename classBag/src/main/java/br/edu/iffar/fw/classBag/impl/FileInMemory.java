package br.edu.iffar.fw.classBag.impl;

import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileInMemory implements Serializable {
    String fileName;
    long size;

    @EqualsAndHashCode.Exclude
    byte[] data;


}