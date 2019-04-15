package jfws.app.demo.ecosystem;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Slider;
import jfws.feature.ecosystem.Ecosystem;
import jfws.feature.ecosystem.Plant;
import jfws.feature.ecosystem.Population;
import jfws.feature.ecosystem.simulation.EcosystemSimulation;
import jfws.feature.ecosystem.simulation.LotkaVolterraSimulation;
import jfws.feature.world.attribute.magic.ManaLevel;
import jfws.feature.world.attribute.rainfall.RainfallLevel;
import jfws.feature.world.attribute.temperature.TemperatureLevel;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class EcosystemController {

	private Plant plant0 = new Plant("Grass", 2.0, 1.0);
	private Plant plant1 = new Plant("Tree", 0.1, 1.4);
	private Plant plant2 = new Plant("Shrub", 0.5, 1.2);

	private Ecosystem ecosystem = new Ecosystem(1000.0,
			ManaLevel.AVERAGE_MANA.getValue(),
			RainfallLevel.AVERAGE.getValue(),
			TemperatureLevel.WARM.getValue());
	private EcosystemSimulation simulation = new LotkaVolterraSimulation();

	private Map<Plant, XYChart.Series> history = new HashMap<>();
	private XYChart.Series seriesTotal = new XYChart.Series();
	private int step = 0;

	@FXML
	LineChart linechart;

	@FXML
	private Slider temperatureSlider;

	@FXML
	private Slider rainfallSlider;

	@FXML
	private Slider manaSlider;

	public EcosystemController() {
		log.info("EcosystemController()");

		ecosystem.addPopulation(new Population(plant0, 100.0));
		ecosystem.addPopulation(new Population(plant1, 100.0));
		ecosystem.addPopulation(new Population(plant2, 100.0));
	}

	@FXML
	private void initialize() {
		log.info("initialize()");

		seriesTotal.setName("Total");
		linechart.getData().add(seriesTotal);

		temperatureSlider.setValue(ecosystem.getTemperature());
		temperatureSlider.valueProperty().addListener((
				observable, oldValue, newValue) -> onTemperatureChanged((Double) newValue));

		rainfallSlider.setValue(ecosystem.getRainfall());
		rainfallSlider.valueProperty().addListener((
				observable, oldValue, newValue) -> onRainfallChanged((Double) newValue));

		manaSlider.setValue(ecosystem.getMana());
		manaSlider.valueProperty().addListener((
				observable, oldValue, newValue) -> onManaChanged((Double) newValue));

		updateChart();
	}

	private void updateChart() {
		log.info("updateChart()");

		double total = 0;

		for(Population population : ecosystem.getPopulations()) {
			Plant plant = population.getPlant();
			XYChart.Series series = history.get(plant);

			if(series == null) {
				series = new XYChart.Series();
				history.put(plant, series);
				linechart.getData().add(series);
			}

			series.setName(plant.getName());
			series.getData().add(new XYChart.Data(step, population.getArea()));

			total += population.getArea();
		}

		seriesTotal.getData().add(new XYChart.Data(step, total));
	}

	@FXML
	public void onSimulationStep() {
		log.info("onSimulationStep()");

		simulation.update(ecosystem);
		step++;
		updateChart();
	}

	@FXML
	public void onTemperatureChanged(double newValue) {
		log.info("onTemperatureChanged(): {}", newValue);
		ecosystem.setTemperature(newValue);
	}

	@FXML
	public void onRainfallChanged(double newValue) {
		log.info("onRainfallChanged(): {}", newValue);
		ecosystem.setRainfall(newValue);
	}

	@FXML
	public void onManaChanged(double newValue) {
		log.info("onManaChanged(): {}", newValue);
		ecosystem.setMana(newValue);
	}
}
