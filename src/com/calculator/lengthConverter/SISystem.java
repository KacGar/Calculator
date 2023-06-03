package com.calculator.lengthConverter;

public enum SISystem {
    NANOMETER {
        @Override
        public double rate() {
            return 1_000_000_000;
        }
    },
    MICROMETER {
        @Override
        public double rate() {
            return 1_000_000;
        }
    },
    MILIMETER {
        @Override
        public double rate() {
            return 1000;
        }
    },
    CENTIMETER {
        @Override
        public double rate() {
            return 100;
        }
    },
    METER {
        @Override
        public double rate() {
            return 1;
        }
    },
    KILOMETER {
        @Override
        public double rate() {
            return 0.001;
        }
    },
    INCH {
        @Override
        public double rate() {
            return 39.370_079_7;
        }
    },
    FOOT {
        @Override
        public double rate() {
            return 3.2808399;
        }
    },
    YARDS {
        @Override
        public double rate() {
            return 1.093_613_3;
        }
    },
    MILES {
        @Override
        public double rate() {
            return 0.000_621_371_192;
        }
    },
    NAUTICAL_MILES{
        @Override
        public double rate() {
            return 0.000_539_956_803;
        }
    };

    public abstract double rate();
}
