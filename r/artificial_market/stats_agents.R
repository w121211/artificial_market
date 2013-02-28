source("~/git/artificial_market/r/artificial_market/global.R")
library("PerformanceAnalytics")
FOLDER = "EXP-01/rf-sm-nn-10-1.0-0.00/"
FILE = 'agent'

i = 1;
agt <- read.table(paste(ROOT, FOLDER, FILE, i-1, sep=""), header=TRUE, sep=",")
lft <- subset(agt, id.contains("L"))

SIMULATION_RUN = 1
ll <- rep(NA, SIMULATION_RUN) # LFT & LFT
lh <- rep(NA, SIMULATION_RUN) # LFT & HFT
hh <- rep(NA, SIMULATION_RUN) # HFT & HFT

for (i in 1:SIMULATION_RUN) {
  INDEX = i-1
  trd <- read.table(paste(ROOT, FOLDER, FILE, INDEX, sep=""), header=TRUE, sep=",")
  trd <- subset(trd, !(t < TRAINING_T))
  trd_ll <- subset(trd, demandAgentType == "L" & supplyAgentType == "L")
  trd_lh <- subset(trd, (demandAgentType == "L" & supplyAgentType == "H") | 
                     (demandAgentType == "H" & supplyAgentType == "L"))
  trd_hh <- subset(trd, demandAgentType == "H" & supplyAgentType == "H")
  
  vol_all = sum(trd$volume)
  ll[i] <- sum(trd_ll$volume) / vol_all
  lh[i] <- sum(trd_lh$volume) / vol_all
  hh[i] <- sum(trd_hh$volume) / vol_all
}

results <- data.frame(ll, lh, hh)