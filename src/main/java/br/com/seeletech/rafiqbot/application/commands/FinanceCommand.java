package br.com.seeletech.rafiqbot.application.commands;

import br.com.seeletech.rafiqbot.domain.OpenWeatherConditionsEnum;
import br.com.seeletech.rafiqbot.domain.StockData;
import br.com.seeletech.rafiqbot.domain.service.RandomCatService;
import br.com.seeletech.rafiqbot.domain.service.StockDataService;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.Random;

@Component
public class FinanceCommand implements SlashCommand {

    private final StockDataService stockDataService;
    private final RandomCatService randomCatService;

    @Value("${app.bot.ephemeral:}")
    private Boolean isEphemeral;

    public FinanceCommand(StockDataService stockDataService, RandomCatService randomCatService) {
        this.stockDataService = stockDataService;
        this.randomCatService = randomCatService;
    }

    @Override
    public String getName() {
        return "finance";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) throws Exception {

        StockData stockData = this.stockDataService.getFinanceData(
                event.getOption("finance_symbols")
                        .flatMap(ApplicationCommandInteractionOption::getValue)
                        .map(ApplicationCommandInteractionOptionValue::asString)
                        .orElseThrow(() -> new Exception("finance symbols with problems")));

        Random numGen = new Random();
        Color color = Color.of(numGen.nextInt(256), numGen.nextInt(256), numGen.nextInt(256));
        String image = this.randomCatService.getRandomCatUrl();

        EmbedCreateSpec embed = EmbedCreateSpec.builder()
                .color(color)
                .title(stockData.getData().get(0).getName())
                .description("")
                .thumbnail(image)
                .addField("Price", "$ " + stockData.getData().get(0).getPrice(), false)
                .addField("Day High", "$ " + stockData.getData().get(0).day_high, false)
                .addField("Day Low", "$ " + stockData.getData().get(0).day_low, false)
                .addField("Day Open", "$ " + stockData.getData().get(0).day_open, false)
                .addField("Day Change", "$ " + stockData.getData().get(0).day_change, false)
                .addField("Reference", "https://stockdata.org", false)
                .footer("seeletech.com.br", "https://static.wikia.nocookie.net/evangelion/images/8/8e/SEELE_Logo.png/revision/latest?cb=20120312002542")
                .build();

        return event.reply()
                .withEphemeral(isEphemeral)
                .withEmbeds(embed);
    }
}
