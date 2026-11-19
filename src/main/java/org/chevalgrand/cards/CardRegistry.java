package org.chevalgrand.cards;

import basemod.BaseMod;
import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import org.chevalgrand.ChevalGrandSlayMod;

public final class CardRegistry {
    private CardRegistry() {
    }

    public static void registerSaveField() {
        BaseMod.addSaveField(ChevalGrandSlayMod.id("Stamina"), new Stamina());
    }

    public static void registerCards() {
        BaseMod.addDynamicVariable(new DynamicVariable() {
            @Override public String key() { return ChevalGrandSlayMod.id("Stamina"); }
            @Override public boolean isModified(AbstractCard card) { return value(card) != baseValue(card); }
            @Override public int value(AbstractCard card) { return ((RaceCard) card).remaining; }
            @Override public int baseValue(AbstractCard card) { return ((RaceCard) card).maxStamina; }
            @Override public boolean upgraded(AbstractCard card) { return false; }
        });

        BaseMod.addCard(new Strike());
        BaseMod.addCard(new Defend());
        BaseMod.addCard(new ALittleCourage());
        BaseMod.addCard(new LightStep());
        BaseMod.addCard(new DisrupTheFormation());
        BaseMod.addCard(new StepSteady());
        BaseMod.addCard(new ReorganizeThePace());
        BaseMod.addCard(new HandoverMoment());
        BaseMod.addCard(new CushionLanding());
        BaseMod.addCard(new SidewaysEvasion());
        BaseMod.addCard(new BriefRestRestart());
        BaseMod.addCard(new UnwindMomentum());
        BaseMod.addCard(new ReorderNotes());
        BaseMod.addCard(new AroundTheBend());
        BaseMod.addCard(new ReviewThePace());
        BaseMod.addCard(new ClearDistractions());
        BaseMod.addCard(new PierceTheGap());
        BaseMod.addCard(new PrepareEarly());
        BaseMod.addCard(new RapidCombination());
        BaseMod.addCard(new TearOpenDefense());
        BaseMod.addCard(new BreakTheOpening());
        BaseMod.addCard(new PrepareForNextStep());
        BaseMod.addCard(new GuardTheFinish());
        BaseMod.addCard(new SpreadTheOpening());
        BaseMod.addCard(new CountTheCost());
        BaseMod.addCard(new FateEcho());
        BaseMod.addCard(new SteadyPace());
        BaseMod.addCard(new Reschedule());
        BaseMod.addCard(new BalanceTheWeight());
        BaseMod.addCard(new FlowingAssault());
        BaseMod.addCard(new CutOffRetreat());
        BaseMod.addCard(new OldTraceReturnsEdge());
        BaseMod.addCard(new LendReserve());
        BaseMod.addCard(new SportsDrinkSupply());
        BaseMod.addCard(new ReturnMarker());
        BaseMod.addCard(new ReplanTheRoute());
        BaseMod.addCard(new SmoothBreathing());
        BaseMod.addCard(new CorrectTheFormation());
        BaseMod.addCard(new RepurposeBulwark());
        BaseMod.addCard(new WaitAndSee());
        BaseMod.addCard(new DrawFinishLine());
        BaseMod.addCard(new DisruptFootwork());
        BaseMod.addCard(new OldRoadWarmth());
        BaseMod.addCard(new RemainingUsesSense());
        BaseMod.addCard(new EarthshakingStep());
        BaseMod.addCard(new FinalSprint());
        BaseMod.addCard(new WinningLine());
        BaseMod.addCard(new FinalChainStep());
        BaseMod.addCard(new WinningShift());
        BaseMod.addCard(new FateGuidance());
        BaseMod.addCard(new UnwaveringStep());
        BaseMod.addCard(new WinningRhythm());
        BaseMod.addCard(new WeightGain());
        BaseMod.addCard(new Ball());
        BaseMod.addCard(new EmptyShell());
        BaseMod.addCard(new EnergyDrink());
        BaseMod.addCard(new LittleBravePlus());
        BaseMod.addCard(new AllOut());
        BaseMod.addCard(new AimForTheFinish());
        BaseMod.addCard(new BorrowFromThePast());
        BaseMod.addCard(new BorrowForceEscape());
        BaseMod.addCard(new ChainStep());
        BaseMod.addCard(new ChaseTheWind());
        BaseMod.addCard(new ClamToFight());
        BaseMod.addCard(new CounterAttack());
        BaseMod.addCard(new Domain());
        BaseMod.addCard(new EchoOfTheFinish());
        BaseMod.addCard(new FillTheGap());
        BaseMod.addCard(new FinalKick());
        BaseMod.addCard(new FinishLineEcho());
        BaseMod.addCard(new ForgeAhead());
        BaseMod.addCard(new KeepSomeInReserve());
        BaseMod.addCard(new LongSprint());
        BaseMod.addCard(new MeasuredAdvanceAndRetreat());
        BaseMod.addCard(new NeverStepBehind());
        BaseMod.addCard(new NoConcession());
        BaseMod.addCard(new NoRegretsEnding());
        BaseMod.addCard(new OldWallNewEdge());
        BaseMod.addCard(new OneBeatAhead());
        BaseMod.addCard(new PourIntoOneMove());
        BaseMod.addCard(new PrefectDefend());
        BaseMod.addCard(new RegainTheEdge());
        BaseMod.addCard(new RenewTheContest());
        BaseMod.addCard(new RenewTheOldPact());
        BaseMod.addCard(new ReserveAStep());
        BaseMod.addCard(new RewriteFate());
        BaseMod.addCard(new SubZero());
        BaseMod.addCard(new SuppressTheirSpirit());
        BaseMod.addCard(new TurnTheOldPage());
        BaseMod.addCard(new TurnTogether());
        BaseMod.addCard(new WideView());
    }
}
