package com.hastings.router.route.service;

import com.hastings.router.route.model.AppRoute;
import com.hastings.router.route.model.RouteConfig;
import com.hastings.router.route.repository.InMemoryRouteConfigRepositoryImpl;
import com.hastings.router.route.repository.RouteConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 *
 */
@Service
public class RouteConfigServiceImpl implements RouteConfigService {

    private static final Logger LOG = LoggerFactory.getLogger(InMemoryRouteConfigRepositoryImpl.class);

    @Autowired
    private RouteConfigRepository routeConfigRepository;

    public static Stream<MatchResult> findAll(Scanner s, Pattern pattern) {
        return StreamSupport.stream(new Spliterators.AbstractSpliterator<MatchResult>(
                1000, Spliterator.ORDERED | Spliterator.NONNULL) {
            public boolean tryAdvance(Consumer<? super MatchResult> action) {
                if (s.findWithinHorizon(pattern, 0) != null) {
                    action.accept(s.match());
                    return true;
                } else return false;
            }
        }, false);
    }

    @Override
    public RouteConfig saveRouteConfig(RouteConfig routeConfig) {
        return routeConfigRepository.save(routeConfig);
    }

    @Override
    public Optional<RouteConfig> getRouteConfig(String id) {
        return routeConfigRepository.getRouteConfig(id);
    }

    /**
     * calculates the app route to use
     *
     * @param policyNumber
     * @param routeConfig
     * @return
     */
    @Override
    public AppRoute calculateAppRoute(String policyNumber, RouteConfig routeConfig) {

        if (routeConfig.getPattern().length() > policyNumber.length()) {
            throw new IllegalStateException("The route config pattern has more characters than the policy number");
        }

        AppRoute appRoute = new AppRoute();

        //only apply routing if config status is 1
        if (routeConfig.getStatus() == 1) {

            //Get the pattern for this routeConfig
            String pattern = routeConfig.getPattern();

            List<Integer> indexesToUse = getIndexFromPattern(pattern);
            int sum = getIntegerSum(policyNumber, indexesToUse);
            LOG.debug(" sum is {}", sum);

            List<Integer> gotten = routeConfig.getSum();

            boolean status = gotten.stream().anyMatch(i -> i.equals(sum));

            LOG.debug("Status code {}", routeConfig.getStatus());
            if (status) {
                appRoute.setAppPath(routeConfig.getAppPath());
            } else {
                appRoute.setAppPath("unresolved");
            }

            appRoute.setId(routeConfig.getId());
            appRoute.setPolicyNumber(policyNumber);
            appRoute.setStatusCode(String.valueOf(routeConfig.getStatus()));

        } else if (routeConfig.getStatus() == 2) {
            appRoute.setAppPath(routeConfig.getAppPath());
            appRoute.setStatusCode("2");
        } else {
            appRoute.setAppPath("");
            appRoute.setStatusCode("0");
        }

        return appRoute;
    }

    @Override
    public List<RouteConfig> getRouteConfigs() {
        return routeConfigRepository.getAllRouteConfigs();
    }

    /**
     * Find the indexes of the # in this pattern String.
     *
     * @param pattern the pattern String to apply to the policy numbers
     * @return the a list of the indexes within the pattern where the hashes are found.
     */
    protected List<Integer> getIndexFromPattern(String pattern) {

        List<Integer> indexesToUse = Stream.of(pattern).map(Scanner::new).flatMap((Scanner s) -> findAll(s, Pattern.compile("#")))
                .map(MatchResult::start).collect(Collectors.toList());

        LOG.debug("indexesToUse {}", indexesToUse);

        return indexesToUse;
    }

    /**
     * Find the numbers in the policy id that require adding together, and add them together.
     *
     * @param policyNumber the policy number to run the check against
     * @param indexesToUse the indexes to use in the policy number to add.
     * @return the sum of the units in the policy id as determined from the indexes to use.
     */
    protected int getIntegerSum(String policyNumber, List<Integer> indexesToUse) {
        List<Integer> numbersToAdd = indexesToUse.stream().map(policyNumber::charAt).map(Character::getNumericValue).
                collect(Collectors.toList());
        LOG.debug("numbers to add {}", numbersToAdd);
        int sum = numbersToAdd.stream().mapToInt(i -> i).sum();
        return sum;
    }
}
