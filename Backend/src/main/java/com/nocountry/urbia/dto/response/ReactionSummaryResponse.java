package com.nocountry.urbia.dto.response;

import java.util.ArrayList;
import java.util.List;

public class ReactionSummaryResponse {
    private ReactionTypeDetail recomendar;
    private ReactionTypeDetail celebrar;
    private ReactionTypeDetail apoyar;
    private ReactionTypeDetail desaprobar;

    public ReactionSummaryResponse() {
        this.recomendar = new ReactionTypeDetail();
        this.celebrar = new ReactionTypeDetail();
        this.apoyar = new ReactionTypeDetail();
        this.desaprobar = new ReactionTypeDetail();
    }

    public ReactionTypeDetail getRecomendar() {
        return recomendar;
    }
    public void setRecomendar(ReactionTypeDetail recomendar) {
        this.recomendar = recomendar;
    }
    public ReactionTypeDetail getCelebrar() {
        return celebrar;
    }
    public void setCelebrar(ReactionTypeDetail celebrar) {
        this.celebrar = celebrar;
    }
    public ReactionTypeDetail getApoyar() {
        return apoyar;
    }
    public void setApoyar(ReactionTypeDetail apoyar) {
        this.apoyar = apoyar;
    }
    public ReactionTypeDetail getDesaprobar() {
        return desaprobar;
    }
    public void setDesaprobar(ReactionTypeDetail desaprobar) {
        this.desaprobar = desaprobar;
    }

    public static class ReactionTypeDetail {
        private int count;
        private List<String> usuarios;

        public ReactionTypeDetail() {
            this.count = 0;
            this.usuarios = new ArrayList<>();
        }

        public int getCount() {
            return count;
        }
        public void setCount(int count) {
            this.count = count;
        }
        public List<String> getUsuarios() {
            return usuarios;
        }
        public void setUsuarios(List<String> usuarios) {
            this.usuarios = usuarios;
        }
    }
}
